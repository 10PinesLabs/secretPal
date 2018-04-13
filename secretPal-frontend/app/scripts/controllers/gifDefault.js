'use strict';

var app = angular.module('secretPalApp');
app.controller('GifDefaultController', function($scope, $route, GifDefaultService, SweetAlert) {

  GifDefaultService.get(
    function(defaultGif){
      $scope.defaultGifUrl = defaultGif.url;
    });

  $scope.change = function(){
    /*SweetAlert.swal({
        title: "Estás seguro?",
        text: "El gif default será modificado",
        type: "warning",
        allowOutsideClick: false,
        showConfirmButton: true,
        showCancelButton: true,
        closeOnConfirm: false,
        closeOnCancel: true,
        confirmButtonText: "¡Si!",
        confirmButtonColor: "#68bd46",
        cancelButtonText: "Cancelar",
        cancelButtonColor: '#FFFFFF',
      },
      function (isConfirm) {
        if(isConfirm) {*/
          GifDefaultService.set($scope.defaultGifUrl);
        /*}
      });*/
  };
});


