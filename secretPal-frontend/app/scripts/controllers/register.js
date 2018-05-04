'use strict';

angular.module('secretPalApp')
  .controller('RegisterController', function ($scope, SweetAlert, Account, $location) {
    $scope.userName = "";
    $scope.password1 = "";
    $scope.password2 = "";
    $scope.email = "";

    $scope.registerNewUser = function () {
      if($scope.password1 === $scope.password2) {
        SweetAlert.swal({
          title: "Registro enviado",
          text: "Espere un momento, por favor",
          duration: 3,
          showConfirmButton: false
        });
        Account.register({userName: $scope.userName, password: $scope.password1, email: $scope.email});
      }else{
        SweetAlert.swal("Las passwords no coinciden", "Por favor revisalas antes de enviar el formulario", "error");
      }
    };

    $scope.returnToMain = function() {
      return $location.path("/");
    };
  });
