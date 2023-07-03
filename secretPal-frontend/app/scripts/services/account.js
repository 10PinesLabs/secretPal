'use strict';

angular.module('secretPalApp')
  .service('Account', function ($http, $rootScope, SweetAlert, $location, Token) {
    function buildRoute(path) {
      var route = '/api/auth';
      return route + path;
    }

    var self = this;

    self.getProfile = function () {
      return $http.get(buildRoute('/me'), {
        headers: {
          Authorization: Token.getToken()
        }
      }).then(function (response) {
        $rootScope.loggedUser = response.data;
        return response.data;
      });
    };

    self.getCurrentProfile = function () {
      return $rootScope.loggedUser;
    };

    self.isAuthenticated = function () {
      return Token.isAuthenticated();
    };

    self.logout = function () {
      return Token.logout();
    };

    self.getAdmins = function () {
      return $http.get(buildRoute('/admin'));
    };

    self.isAdmin = function () {
      if ($rootScope.loggedUser !== undefined) {
        return $rootScope.loggedUser.admin;
      }
    };

    self.makeAdmin = function (worker, callback) {
      $http.post(buildRoute('/admin'), worker).success(function (data) {
        callback();
      })
    };

  });
