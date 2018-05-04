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
  });


