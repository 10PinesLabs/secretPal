'use strict';

var app = angular.module('secretPalApp');

app.controller('CustomRuleController', function($scope, $route, MailService,$filter, SweetAlert, WorkerService, CustomRuleService) {

  WorkerService.all(function (data) {
    $scope.workers = data;
  });

  $scope.createRule = function(){
    var from = $scope.workerGiver.id;
    var to = $scope.workerReceiver.id;
    CustomRuleService.new(from, to);
  }

});
