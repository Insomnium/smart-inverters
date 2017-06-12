/*******************************************************
 The predix-seed Express web application includes these features:
 * routes to mock data files to demonstrate the UI
 * passport-predix-oauth for authentication, and a sample secure route
 * a proxy module for calling Predix services such as asset and time series
 *******************************************************/

var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser'); // used for session cookie
var bodyParser = require('body-parser');
var session = require('express-session'); // simple in-memory session is used here. use connect-redis for production!!
var config = require('./config/app-config');
var proxy = require('./proxy'); // used when requesting data from real services.
// configure passport for authentication with UAA
var passportConfig = require('./passport-config');
//var redis = require('./redis');

/**********************************************************************
 SETTING UP EXRESS SERVER
 ***********************************************************************/
var app = express();

app.set('trust proxy', 1);
//Initializing application modules
app.use(cookieParser('predixsample'));
// Initializing default session store
// *** Use this in-memory session store for development only. Use redis for prod. **
app.use(session({
  secret: 'predixsample',
  name: 'nsessionid',
  proxy: true,
  resave: true,
  saveUninitialized: true
}));


/****************************************************************************
 SET UP EXPRESS ROUTES
 *****************************************************************************/


if (config.isUaaConfigured()) {
  var passport = passportConfig.configurePassportStrategy(config);
  app.use(passport.initialize());
  // Also use passport.session() middleware, to support persistent login sessions (recommended).
  app.use(passport.session());

  console.log("Setting passport authentication strategy with endpoint "+config.uaaUrl);


  //login route redirect to predix uaa login page
  app.get('/login', passport.authenticate('predix', {'scope': ''}), function (req, res) {
    // The request will be redirected to Predix for authentication, so this
    // function will not be called.
  });

  //callback route redirects to secure route after login
  app.get('/callback', passport.authenticate('predix', {failureRedirect: '/'}), function (req, res) {
    res.redirect('/');
  });

  //logout route
  app.get('/logout', function (req, res) {
    req.session.destroy();
    req.logout();
    passportConfig.reset(); //reset auth tokens
    res.redirect(config.uaaUrl + '/logout?redirect=' + config.appUrl);
  });

  //Use this route to make the entire app secure.  This forces login for any path in the entire app.
  app.use('/', passport.authenticate('main', {noredirect: false}),
    express.static(path.join(__dirname, '../public'))
  );

  // access real Predix services using this route.
  // the proxy will add UAA token and Predix Zone ID.
  app.use('/predix-api', passport.authenticate('main', {noredirect: true}), proxy.router);

} else {
  app.use(express.static(path.join(__dirname, '../public')));
}


app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));



var server = app.listen(process.env.VCAP_APP_PORT || 5000, function () {

  console.log("************* Server started on port: " + server.address().port + " *************");
});


////// error handlers //////
// no stacktraces leaked to user
app.use(function (err, req, res, next) {
  console.error(err.stack);
  if (!res.headersSent) {
    res.status(err.status || 500);
    res.send({
      message: err.message,
      error: err
    });
  }
});

module.exports = app;

