'use strict';

angular.module('secretPalApp').service('FriendRelationService', function ($http, SweetAlert) {

  function buildRoute(path) {
    var route = '/api/friendRelation';
    return route + path;
  }

  function successMsg(title, msg) {
    SweetAlert.swal({
      title:title,
      text:msg,
      type:"success",
      showConfirmButton: false,
      timer: 800
    })  ;
  }

  function errorMsg(msg) {
    SweetAlert.swal({
      title: "Algo salio mal",
      text: msg,
      type:"error",
      showConfirmButton: false,
      timer: 800
    });
  }

  this.all = function (callback) {
    $http.get(buildRoute('/')).success(function (data) {
      callback(data);
    }).error(function () {
      errorMsg("No se pudieron cargar las relaciones.");
    });
  };

  this.delete = function (idGiver, idReceiver, successFunction) {
    $http.delete(buildRoute('/' + idGiver + '/' + idReceiver)).success(function () {
      successFunction();
    }).error(function () {
      errorMsg("No se pudo borrar esta relación");
    });
  };

  this.getFriend = function (worker, callback) {
    return $http.get(buildRoute('/friend/' + worker.id)).then(function (data) {
        callback(data);
      });
  };

  this.getAvailableFriend = function (worker, callback) {
    return $http.get(buildRoute('/posibleFriend/' + worker.id)).then(function (data) {
        callback(data);
      },
      function () {
        errorMsg("No se puede conseguir el/los posible/s pino/s invisible/s. Intente nuevamente mas tarde");
      });
  };

  this.allPosibleRelations = function(callback) {
    $http.get(buildRoute('/posibilities')).success(function (data) {
      callback(data);
    }).error(function () {
      errorMsg("No se pudo procesar el pedido");
    });
  };

  this.allInmutableRelations = function(callback) {
    $http.get(buildRoute('/inmutables')).success(function (data) {
      callback(data);
    }).error(function () {
      errorMsg("La asignación automática falló.");
    });
  };

  this.update = function (giver, receiver, callback) {
    $http.put(buildRoute('/update/' + giver.id + '/' + receiver.id)).success(function () {
      successMsg("Asignación exitosa", "Ahora " + giver.fullName + " le regala a " + receiver.fullName + "!");
      callback();
    }).error(function () {
      errorMsg("No se pudo procesar el pedido");
    });
  };

  this.delete = function(giver, successFunction) {
    $http.delete(buildRoute('/' + giver.id)).
    success(function() {
      successMsg("Relación eliminada exitosamente", "Ahora " + giver.fullName + " no es amigo invisible de nadie :(");
      successFunction();
    });
  };

  this.lock = function(relation, callback) {
    $http.put(buildRoute('/' + relation.id + '/makeImmutable'), null).success(function () {
      successMsg("La relación fue fijada exitosamente!");
      callback();
    }).error(function () {
      errorMsg("No se pudo procesar el pedido");
    });
  }

});
