/*
 This module reads config settings from localConfig.json when running locally,
 or from the VCAPS environment variables when running in Cloud Foundry.
 */

var settings = {};
var globalSettings = require('./globalConfig.json');
var predixSettings = require('./predixServiceStubConfig.json');
var url = require('url');


if (process.env.VCAP_SERVICES) {
  settings.services = JSON.parse(process.env.VCAP_SERVICES)
} else {
  settings.services = predixSettings.VCAP_SERVICES;
}

if (process.env.VCAP_APPLICATION) {
  settings.appUrl = 'https://' + JSON.parse(process.env.VCAP_APPLICATION).uris[0];
} else {
  settings.appUrl = globalSettings.appUrl;
}

//console.log(JSON.stringify(settings));

var services = settings.services;

settings.isUaaConfigured = function () {
  return services && services['predix-uaa'] &&
    services['predix-uaa'][0] &&
    services['predix-uaa'][0].credentials &&
    services['predix-uaa'][0].credentials.uri.indexOf('https') === 0;
};


if (settings.isUaaConfigured()) {
  settings.uaaUrl = services['predix-uaa'][0].credentials.uri;
  settings.tokenUrl = settings.uaaUrl;
  settings.callbackUrl = settings.appUrl + '/callback';
  settings.clientId = globalSettings.clientId;
  settings.base64ClientCredential = globalSettings.base64ClientCredential;

  // harmonize uri and zone id
  for (var i in services) {
    var service = services[i];
    service.forEach(function (servInstance) {
      var urlStr = servInstance.credentials.query ? servInstance.credentials.query.uri : servInstance.credentials ? servInstance.credentials.uri : servInstance.uri;

      /**** redis specific ***/
      var host = servInstance.credentials.host;
      var port = servInstance.credentials.port;
      var password = servInstance.credentials.password;

      if (!host) {
        var urlObj = url.parse(urlStr);
        var uri = urlObj.protocol + '//' + urlObj.host;
        var zone = servInstance.credentials.query ? servInstance.credentials.query['zone-http-header-value'] :
          servInstance.credentials ? servInstance.credentials.zone['http-header-value'] : servInstance.zone['zone-http-header-value'];
        servInstance.uri = uri;
        servInstance.zoneId = zone;
      }

      servInstance.host = host;
      servInstance.port = port;
      servInstance.password = password;
    });
  }
} else {
  console.log('UAA is not configured for this application.');
  console.log('"predix-uaa" service needs to be defined in predix console, ' +
    'or if running locally the same VCAPS variable needs to be present in predixServiceStubConfig.json ');
}


// add local services to the list of settings, so the proxy module sets up the proxy for them
for (var i in globalSettings.services) {
  services[i] = globalSettings.services[i];
}


module.exports = settings;

