'use strict';

angular.module('secretPalApp')
  .service('Token', function ($rootScope) {
    var self = this;

    self.saveToken = function (aToken) {
      localStorage.token = aToken;
    };

    self.getToken = function () {
      return localStorage.token;
    };

    self.isAuthenticated = function () {
      return !! self.getToken();
    };

    self.logout = function () {
      $rootScope.loggedUser = undefined;
      window.localStorage.clear();
    };

    self.authURL = function () {
      var apiURL = location.protocol + "//" + window.location.hostname + (location.port ? ':' + location.port : '');
      return encodeURI("https://backoffice.10pines.com/auth/sign_in" +
        "?redirect_url=" + apiURL + "/api/auth/callback" + location.hash.substring(1) +
        "&app_id=secret-pal");
    };
  });


