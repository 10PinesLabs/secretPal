'use strict';

angular.module('secretPalApp')
  .service('Token', function ($rootScope) {

    this.saveToken = function (aToken) {
      sessionStorage.token = aToken;
    };

    this.getToken = function () {
      return sessionStorage.token;
    };

    this.isAuthenticated = function () {
      return sessionStorage.token !== undefined;
    };

    this.logout = function () {
      $rootScope.loggedUser = undefined;
      return sessionStorage.clear("token");
    };
  });


