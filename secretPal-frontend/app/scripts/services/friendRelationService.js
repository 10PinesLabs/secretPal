'use strict';

angular.module('secretPalApp').service('FriendRelationService', function($http, SweetAlert) {

  function buildRoute(path) {
    var route = '/api/friendRelation';
    return route + path;
  }

  function successMsg(msg) {
    SweetAlert.swal("", msg, "success");
  }

  function errorMsg(msg) {
    SweetAlert.swal("Algo salio mal",msg, "error");
  }

  this.all = function(callback) {
    $http.get(buildRoute('/')).
      success(function(data) {
        callback(data);
      }).
      error(function() {
        errorMsg("No se pudo procesar el pedido");
      });
  };

  this.new = function(relations) {
    $http.post(buildRoute('/'), relations).
      success(function() {
        successMsg("La asignacion fue exitosa");
      }).
      error(function() {
        errorMsg("No se pudo procesar el pedido");
      });
  };

  this.delete = function(idGiver, idReceiver, successFunction) {
    $http.delete(buildRoute('/' + idGiver + '/' + idReceiver)).
      success(function() {
        successFunction();
      }).
      error(function() {
        errorMsg("No se pudo borrar esta relacion");
      });
  };

  this.getFriend = function(worker, callback) {
    return $http.post(buildRoute('/friend'), worker).
        then(function(data) {
          callback(data);
        },
        function() {
          errorMsg("Intente nuevamente");
        });
  };

});
