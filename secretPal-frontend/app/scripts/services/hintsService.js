'use strict';

angular.module('secretPalApp').service('HintsService', function ($http, SweetAlert) {

  function buildRoute(path) {
    var route = '/api/friendRelation';
    return route + path;
  }

  function errorMsg(msg) {
    SweetAlert.swal("Algo salio mal", msg, "error");
  }

  this.all = function (user, callback) {
    $http.get(buildRoute('/hintsFrom/' + user.worker.id)).success(function (data) {
      callback(data);
    }).error(function () {
      errorMsg("No se pudo cargar la lista de pistas, inténtlo de nuevo más tarde.");
    });
  };

  this.hintsLimit = function (callback) {
    $http.get(buildRoute('/hintsLimit')).success(function (data) {
      callback(data);
    }).error(function () {
      errorMsg("No limit found");
    });
  };

  this.new = function (user, hint, successFunction) {
    $http.post(buildRoute('/hintsFrom/' + user.worker.id), hint).success(function (id) {
      successFunction(id);
    }).error(function () {
      errorMsg("No se pudo agregar la pista, por favor inténtelo de nuevo.");
    });
  };

  this.delete = function (user, hint, successFunction) {
    $http.delete(buildRoute('/hintsFrom/' + user.worker.id + "/" + hint)).success(function (data) {
      successFunc tion(data);
    });
  };

  this.update = function (user, hint, newHint, successFunction) {
    $http.put(buildRoute('/hintsFrom/' + user.worker.id + "/" + hint), newHint).success(function (data) {
      successFunction(data);
    });
  };


});
