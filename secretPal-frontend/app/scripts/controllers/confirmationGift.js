'use strict';

var app = angular.module('secretPalApp');
app.controller('ConfirmationGiftController', function ($scope, $http, $modal, $rootScope, WorkerService, $filter, $location, user, SweetAlert) {

  WorkerService.all(function (data) {
    $scope.workers = data;
  });

  function buildRoute(path) {
    var route = '/api/auth';
    return route + path;
  }

  $scope.markReceived = function(id){
    var self = this;
    return $http.put(buildRoute('/confirmationGift/' + id), id).then(function () {
      SweetAlert.swal("¡Regalo marcado como recibido!","con la fecha de hoy", "success");
      WorkerService.all(function (data) {
        $scope.workers = data;
      });
    }).catch(function (error) {
      SweetAlert.swal("No se ha marcado como recibido", "", "error");
      $location.path('/confirmationGift');
    });
  };
});





