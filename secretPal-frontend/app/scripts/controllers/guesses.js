'use strict';

angular.module('secretPalApp')
  .controller('GuessesController', function ($scope, $http, user, GuessesService, $modal, $log, SweetAlert) {
      $scope.hints = [];

      GuessesService.all(user, function (data) {
        $scope.hints = data;
      });

    }
  )
