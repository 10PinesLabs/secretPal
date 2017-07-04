'use strict';

angular.module('secretPalApp')
  .controller('LoginController', function ($scope, SweetAlert, Account) {
    $scope.userName = "";
    $scope.password = "";

    $scope.internalAuthenticate = function () {

      SweetAlert.swal({
        title: "Logeandose",
        text: "Entrando en la Matrix...",
        showConfirmButton: false,
      });

      Account.login({userName: $scope.userName, password: $scope.password});
    };
  });
