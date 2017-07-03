'use strict';

var app = angular.module('secretPalApp');
app.controller('MailController', function($scope, $route, MailService, SweetAlert) {

  MailService.get( function(mail){ $scope.mail = mail;});

  $scope.change = function(){
    SweetAlert.swal({
        title: "¿Estas seguro?",
        text: "El envio de mails sera modificado",
        type: "warning",
        allowOutsideClick: false,
        showConfirmButton: true,
        showCancelButton: true,
        closeOnConfirm: false,
        closeOnCancel: true,
        confirmButtonText: "¡Si!",
        confirmButtonColor: "#68bd46",
        cancelButtonText: "Cancelar",
        cancelButtonColor: "#b8bdb7",
      },
      function (isConfirm) {
        if(isConfirm) {
          MailService.new($scope.mail);
        }
      });
  };

});
