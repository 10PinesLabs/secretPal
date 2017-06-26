'use strict';

var app = angular.module('secretPalApp');

app.controller('CustomRuleController', function ($scope, $route, MailService, $filter, SweetAlert, WorkerService, CustomParticipantRuleService) {

  WorkerService.all(function (data) {
    $scope.workers = data;
  });

  CustomParticipantRuleService.all(function (data) {
    $scope.rules = data
  })

  $scope.createRule = function () {
    var from = $scope.workerGiver.id;
    var to = $scope.workerReceiver.id;
    CustomParticipantRuleService.new(from, to);
  }

  $scope.deleteWithConfirmationMsg = function (rule) {
    SweetAlert.swal({
        title: "Estas seguro?",
        text: "No vas a poder recuperar esta regla!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Si, borrar!",
        closeOnConfirm: false
      },

      function (isConfirm) {
        if (isConfirm) {
          customRuleService.delete(rule.id, function () {
            $scope.rules = $filter('filter')($scope.rules, {id: '!' + rules.id});
            SweetAlert.swal("Se ha borrado exitosamente");
          });
        }
      });
  };

  $scope.delete = function (rule) {
    if (rule.isRuleActivate) {
      warningMsg("La regla está activada, no se puede borrar");
    } else {
      $scope.deleteWithConfirmationMSg(rule);
    }
  };

  $scope.changeIntention = function (rule) {
    rule.changeRuleIntention;
  }

});
