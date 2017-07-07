'use strict';

angular.module('secretPalApp').service('CustomParticipantRuleService', function ($http, SweetAlert) {
  var self = this;

  self.buildRoute = function (path) {
    var route = '/api/customParticipantRule';
    return route + path;
  };

  self.successMsg = function (msg) {
    SweetAlert.swal("", msg, "success");
  };

  self.errorMsg = function (msg) {
    SweetAlert.swal("Algo salio mal", msg, "error");
  };

  this.allCustomRules = function (callback) {
    $http.get(self.buildRoute('/customRules')).success(function (data) {
      callback(data);
    }).error(function () {
      self.errorMsg("No se pudieron conseguir las reglas, intentelo nuevamente.");
    });
  };

  this.new = function (from, to) {
    return $http.post(self.buildRoute('/' + from + '/' + to));
  };

  this.delete = function (id, successFunction) {
    $http.delete(self.buildRoute('/' + id)).success(function () {
      self.successMsg("Regla eliminada exitosamente");
      successFunction();
    });
  };

  this.notCircularRule = function (callback) {
    $http.get(self.buildRoute('/notCircularRule')).success(function (data) {
      callback(data);
    }).error(function () {
      self.errorMsg("No se pudo cargar la regla circular, intentalo nuevamente.")
    });
  }

  this.notTooCloseBirthdayRule = function (callback) {
    $http.get(self.buildRoute('/notTooCloseBirthdayRule')).success(function (data) {
      callback(data);
    }).error(function () {
      self.errorMsg("No se pudo cargar la regla de los cumpleaños cercanos, intentalo nuevamente.")
    });
  }

  this.updateIsCheckedCircular = function (rule) {
    $http.post(self.buildRoute('/notCircularRule', rule)).success(function () {
      self.successMsg("Se ha actualizado la regla circular");
    }).error(function () {
      self.errorMsg("No se pudo actualizar la regla circular, inténtelo de nuevo mas tarde");
    });
  };

  this.updateIsCheckedBirthday = function (rule) {
    $http.post(self.buildRoute('/notTooCloseBirthdayRule', rule)).success(function () {
      self.successMsg("Se ha actualizado la regla de cumpleaños cercano");
    }).error(function () {
      self.errorMsg("No se pudo actualizar la regla de cumpleaños cercano, inténtelo de nuevo mas tarde");
    });
  };
});
