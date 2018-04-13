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
    $http.get(buildRoute('/gifDefault')).
    success(function(data) {
      successFunction(data);
    }).
    error(function() {
      errorMsg("No se pudieron cargar los regalos.");
    });
  };

  this.update = function(giftDefault) {
    $http.post(buildRoute('/giftsDefault'), giftDefault).
    success(function() {
      successMsg("Se ha actualizado el regalo default");
    }).
    error(function() {
      errorMsg("No se ha podido actualizar el regalo default.");
    });
  };

});
