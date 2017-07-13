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

  $scope.remind = function(){
    SweetAlert.swal({
        title: "¿Estas seguro?",
        text: "Se enviaran mails de recordatorio a aquellos pinos cuyo agasajado cumpla en 2 meses o 2 semanas.",
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
          MailService.remind();
        }
      });
  };

});
