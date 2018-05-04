'use strict';

angular.module('secretPalApp')
  .controller('MainController', function ($scope, Account, Token) {
    Account.getAdmins().then(function (admins) {
      $scope.admins = admins.data;
    });

    $scope.isAuthenticated = function () {
      return (Token.isAuthenticated());
    };

    $scope.authURL = function () {
      const apiURL = location.protocol + "//" + window.location.hostname + (location.port ? ':' + location.port : '');
      return encodeURI("https://backoffice.10pines.com/auth/sign_in" +
        "?redirect_url=" + apiURL + "/api/auth/callback" +
        "&app_id=secret-pal");
    };
  });
