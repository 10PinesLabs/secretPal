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
});
