'use strict';

angular.module('secretPalApp')
  .service('GifDefaultService', function($http, SweetAlert) {

  function buildRoute(path) {
    var route = '/api/auth';
    return route + path;
  }

  function successMsg(msg) {
    SweetAlert.swal("", msg, "success");
  }

  function errorMsg(msg) {
    SweetAlert.swal("Algo salió mal",msg, "error");
  }

  this.get = function(successFunction) {
    $http.get(buildRoute('/defaultGif')).
      success(function(data) {
        successFunction(data);
      }).
      error(function() {
        errorMsg("No se pudo cargar el gif default.");
      });
  };

  this.set = function(defaultGifUrl) {
    $http.post(buildRoute('/defaultGif'), defaultGifUrl).
      success(function() {
        successMsg("Se ha actualizado el gif default");
      }).
      error(function() {
        errorMsg("No se ha podido actualizar el gif default.");
      });
  };

});
