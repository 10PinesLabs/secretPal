'use strict';

var app = angular.module('secretPalApp');
app.controller('GifDefaultController', function($scope, $route, GifDefaultService, SweetAlert) {

  $scope.defaultGifUrl = "https://media.giphy.com/media/3oEhn78T277GKAq6Gc/giphy.gif";

  GifDefaultService.get(
    function(defaultGifUrl){
      $scope.defaultGifUrl = defaultGifUrl;
    });

  $scope.change = function(){
    SweetAlert.swal({
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
        if(isConfirm) {
          GifDefaultService.update($scope.defaultGifUrl);
        }
      });
  };
});


