'use strict';

angular.module('secretPalApp').service('MailService', function($http, SweetAlert) {

  function buildRoute(path) {
    var route = '/api/mail';
    return route + path;
  }

  function successMsg(msg) {
    SweetAlert.swal("", msg, "success");
  }

  function errorMsg(msg) {
    SweetAlert.swal("Algo salió mal",msg, "error");
  }


  this.get = function(successFunction) {
    $http.get(buildRoute('/')).
      success(function(data) {
        successFunction(data);
      }).
      error(function() {
        errorMsg("Inténtelo de nuevo mas tarde. ¿Cuando se dispara esto?");
      });
  };

  this.new = function(mail) {
    $http.post(buildRoute('/'), mail).
      success(function() {
        successMsg("Se ha actualizado la configuración del mail");
      }).
      error(function() {
        errorMsg("No se pudo actualizar la configuración, inténtelo de nuevo mas tarde");
      });
  };

  this.all = function(callback) {
    $http.get(buildRoute('/failedMails') ).
    success(function(data) {
      callback(data);
    }).
    error(function() {
      errorMsg("Inténtelo de nuevo mas tarde. ¿Cuando se dispara esto?");
    });
  };

  this.resendMessage = function (unsentMessage, successFunction) {
    $http.post(buildRoute('/resendMailsFailure'), unsentMessage).
        success(function(){
      successMsg("Se reenvió el mail correctamente");
      successFunction();
    }).error(function () {
      errorMsg("No se pudo reenviar el mail, inténtelo mas tarde");

    });

  };


});
