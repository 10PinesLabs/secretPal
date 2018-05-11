'use strict';

angular.module('secretPalApp')
  .controller('MainController', function ($scope, Account, Token) {
    Account.getAdmins().then(function (admins) {
      $scope.admins = admins.data;
    });

    $scope.isAuthenticated = function () {
      return (Token.isAuthenticated());
    };

    $scope.authURL = Token.authURL;
  });
