/**
 * This module can be used to set up reverse proxying from client to Predix services.
 * It assumes only one UAA instance, one UAA client, and one instance of each service.
 * Use setUaaConfig() and setServiceConfig() for local development.
 * In cloud foundry, set the following environment vars: clientId, base64ClientCredential
 * Info for bound services is read from VCAP environment variables.
 */

var url = require('url');
var express = require('express');
var expressProxy = require('express-http-proxy');
var HttpsProxyAgent = require('https-proxy-agent');
var config = require('./config/app-config');
var router = express.Router();

var corporateProxyServer = process.env.http_proxy || process.env.HTTP_PROXY || process.env.https_proxy || process.env.HTTPS_PROXY;
var corporateProxyAgent;
if (corporateProxyServer) {
  corporateProxyAgent = new HttpsProxyAgent(corporateProxyServer);
}


var getClientToken = function (successCallback, errorCallback) {
  var request = require('request');
  var options = {
    method: 'POST',
    url: config.uaaUrl + '/oauth/token',
    form: {
      'grant_type': 'client_credentials',
      'client_id': config.base64ClientCredential.clientId
    },
    headers: {
      'Authorization': 'Basic ' + config.base64ClientCredential
    }
  };

  request(options, function (err, response, body) {
    if (!err && response.statusCode == 200) {
      // console.log('response from getClientToken: ' + body);
      var clientTokenResponse = JSON.parse(body);
      successCallback(clientTokenResponse['token_type'] + ' ' + clientTokenResponse['access_token']);
    } else if (errorCallback) {
      errorCallback(body);
    } else {
      console.log('ERROR fetching client token: ' + body);
    }
  });
};

var configureProxyForService = function (key, serviceInstance) {

  if (!serviceInstance.uri)
      return;

  console.log('Setting proxy route for service "' + key + '" with endpoint ' + serviceInstance.uri);

  router.use('/' + key, expressProxy(serviceInstance.uri, {
    https: true,

    intercept: function (rsp, data, req, res, cb) {
      res.removeHeader('Access-Control-Allow-Origin');
      cb(null, data);
    },

    decorateRequest: function (req) {
      if (corporateProxyAgent) {
        req.agent = corporateProxyAgent;
      }
      req.headers['Content-Type'] = 'application/json';
      if (serviceInstance.zoneId) {
        req.headers['Predix-Zone-Id'] = serviceInstance.zoneId;
      }

    }
  }));
};

var addClientTokenMiddleware = function (req, res, next) {
  function errorHandler(errorString) {
    // TODO: fix, so it doesn't return a status 200.
    //  Tried sendStatus, but headers were already set.
    res.send(errorString);
  }

  if (req.user) {
    var userToken = req.user.ticket.access_token.split(".")[1];
    var user = JSON.parse(Buffer.from(userToken, 'base64'));
    req.headers["Username"] = user.user_name;
  }

  if (req.session) {
    if (!req.session.clientToken) {
      // console.log('fetching client token');
      getClientToken(function (token) {
        req.session.clientToken = token;
        req.headers['Authorization'] = req.session.clientToken;
        next();
      }, errorHandler);
    } else {
      // console.log('client token found in session');
      req.headers['Authorization'] = req.session.clientToken;
      next();
    }
  } else {
    next(res.sendStatus(403).send('Forbidden'));
  }
};

router.use('/', addClientTokenMiddleware);


if (config.isUaaConfigured())
  Object.keys(config.services).forEach(function (key) {
    var service = config.services[key];

    if (Array.isArray(service)) {
      service.forEach(function (serviceInstance) {
        configureProxyForService(key, serviceInstance);
      });
    } else {
      configureProxyForService(key, service);
    }
  });


module.exports = {
  router: router,
  getClientToken: getClientToken
};

