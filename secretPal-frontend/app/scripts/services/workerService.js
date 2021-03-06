'use strict';

angular.module('secretPalApp').service('WorkerService', function($http, SweetAlert) {

  function buildRoute(path) {
    var route = '/api/worker';
    return route + path;
  }

  function successMsg(msg) {
    SweetAlert.swal("", msg, "success");
  }

  function errorMsg(msg) {
    SweetAlert.swal("Algo salió mal",msg, "error");
  }

  this.all = function(callback) {
    $http.get(buildRoute('/')).
      success(function(data) {
        callback(data);
      }).
      error(function() {
          errorMsg("No se pudieron cargar los participantes, intentelo de nuvo más tarde.");
      });
  };

  this.new = function(worker, successFunction) {
    $http.post(buildRoute('/'), worker).
      success(function(data) {
        successFunction(data);
        successMsg("Este pino fue creado exitosamente");
      }).
      error(function() {
        errorMsg("No se pudo crear este pino.");
      });
  };

  this.changeIntention = function(worker) {
    $http.post(buildRoute('/intention'), worker).
      success(function() {
      });
  };

  this.delete = function(id, successFunction) {
    $http.delete(buildRoute('/' + id)).
      success(function() {
        successMsg("Pino eliminado exitosamente");
        successFunction();
      }).
      error(function() {
        errorMsg("No se pudo eliminar este pino.");
      });
  };

  this.update = function (worker) {
    $http.post(buildRoute('/edit'), worker);
  };

  this.updateGifUrlFor = function (worker) {
    $http.put(buildRoute('/' + worker.id + '/gif'), worker.gifUrl);
  };

});
