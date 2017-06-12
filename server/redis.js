var redis = require('redis');
var config = require('./config/app-config');
var proxy = require('./proxy');



var assetService = config.services['predix-asset']? config.services['predix-asset'][0] : {};
var redisService = config.services['redis-17']? config.services['redis-17'] : {};

var client = redis.createClient(redisService.port, redisService.host,{password: redisService.password});
client.on('connect', function () {
  console.log("************* Connected to redis cache server *************");
  console.log("Caching substation line data ... ");

  proxy.getClientToken(function (token) {

    var request = require('request');
    var options = {
      method: 'GET',
      url: assetService.uri + '/line',
      qs: {pageSize: 1000, filter: 'LinePlacement.GeometryType=DETAIL'},
      headers: {
        'Authorization': token,
        'Predix-Zone-Id': assetService.zoneId,
        'Content-Type': 'application/json'
      }
    };

    var lines = [];
    var assetReq = function () {
      request(options, function (err, response, body) {
        if (!err && response.statusCode == 200) {
          lines = lines.concat(JSON.parse(body));
          console.log('Fetching of line data complete, now caching.');
          client.set('lines', JSON.stringify(lines), function () {
            console.log('Stored ' + lines.length + ' lines in cache.');
          });
        } else if (!err && response.statusCode == 206) {
          console.log('Fetching next page of lines.');
          lines = lines.concat(JSON.parse(body));
          var link = response.headers.link;
          options.url = link.substr(1);
          assetReq();
        } else {
          console.log('ERROR fetching line data from asset manager : ' + err);
        }
      });
    };
    assetReq();

  }, function (error) {
    console.log('ERROR while logging into UAA : ' + error);
  });


});


module.exports = client;
