'use strict';

var app = angular.module('secretPalApp');
app.controller('GiftDefaultController', function($scope, $route, GiftDefaultService, SweetAlert) {

  GiftDefaultService.get(

    function(defaultGift){
      $scope.defaultGift = defaultGift;
    });

  $scope.change = function(){
    SweetAlert.swal({
        title: "Estás seguro?",
        text: "El regalo default será modificado",
        type: "warning",
        allowOutsideClick: false,
        showConfirmButton: true,
        showCancelButton: true,
        closeOnConfirm: false,
        closeOnCancel: true,
        confirmButtonText: "¡Si!",
        confirmButtonColor: "#DD6B55",
        cancelButtonText: "Cancelar",
      },
      function (isConfirm) {
        if(isConfirm) {
          GiftDefaultService.new($scope.defaultGift);
        }
      });
  };
});


