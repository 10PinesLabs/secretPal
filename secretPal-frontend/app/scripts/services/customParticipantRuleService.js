'use strict';

angular.module('secretPalApp').service('CustomRuleService', function ($http, SweetAlert) {

  function buildRoute(path) {
    var route = '/api/customRule';
    return route + path;
  }

  function successMsg(msg) {
    SweetAlert.swal("", msg, "success");
  }

  function errorMsg(msg) {
    SweetAlert.swal("Algo salio mal", msg, "error");
  }

  this.all = function (callback) {
    $http.get(buildRoute('/')).success(function (data) {
      callback(data);
    }).error(function () {
      errorMsg("No se pudo procesar el pedido");
    });
  };

  this.new = function (from, to) {
    $http.post(buildRoute('/' + from + '/' + to)).success(function () {
      successMsg("La nueva regla fue creada");
    }).error(function () {
      errorMsg("No se pudo procesar el pedido");
    });
  };

  this.delete = function (id, successFunction){
    $http.delete(buildRoute('/' + id)).success(function() {
      successMsg("Regla eliminada exitosamente");
      successFunction();
    });
  }

});
