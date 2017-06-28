'use strict';

var app = angular.module('secretPalApp');

app.controller('CustomParticipantRuleController', function ($scope, $route, MailService, $filter, SweetAlert, WorkerService, CustomParticipantRuleService) {

  WorkerService.all(function (data) {
    $scope.workers = data;
  });

  CustomParticipantRuleService.all(function (data) {
    $scope.rules = data
  })

  $scope.createRule = function () {
    var from = $scope.workerGiver.id;
    var to = $scope.workerReceiver.id;

    CustomParticipantRuleService.new(from, to).success(function (rule) {
      $scope.rules = R.append($scope.rules, rule);
      CustomParticipantRuleService.successMsg("La nueva regla fue creada");
    }).error(function () {
      CustomParticipantRuleService.errorMsg("No se pudo crear la regla");
    });
  }

  $scope.deleteWithConfirmationMsg = function (rule) {
    SweetAlert.swal({
        title: "¿Estas seguro?",
        text: "¡No vas a poder recuperar esta regla!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Si, ¡borrar!",
        closeOnConfirm: false
      },

      function (isConfirm) {
        if (isConfirm) {
          CustomParticipantRuleService.delete(rule.id, function () {
            $scope.rules = R.remove($scope.rules, rule);
            SweetAlert.swal("Regla eliminada exitosamente");
          });
        }
      });
  };

  $scope.delete = function (rule) {
    if (rule.isRuleActivate) {
      warningMsg("La regla está activada, no se puede borrar");
    } else {
      $scope.deleteWithConfirmationMsg(rule);
    }
  };

  $scope.changeIntention = function (rule) {
    rule.changeRuleIntention;
  }

  $scope.canDelete = function (rule) {
    return rule.isActivate == false;
  };

});