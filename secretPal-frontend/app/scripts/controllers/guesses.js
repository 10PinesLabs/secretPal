'use strict';

angular.module('secretPalApp')
  .controller('GuessesController', function ($scope, $http, user, GuessesService, WorkerService) {
      $scope.hints = [];
      $scope.guess = null;
      $scope.user = user;
      $scope.today = new Date();


    loadHints();
    loadPossibleSecretPines();

    $scope.diff = function (date) {
      var unDia = 24 * 60 * 60 * 1000; // hora*minuto*segundo*milli
      var birthday = new Date(date);
      birthday.setYear($scope.today.getFullYear());

      return Math.round((birthday.getTime() - $scope.today.getTime()) / unDia);
    };

    $scope.beforeBirthday = function () {
      var date = $scope.user.worker.dateOfBirth;
      var diff = $scope.diff(date);

      return (diff > 0);
    };

    function loadHints() {
      GuessesService.all(user, function (data) {
        $scope.hints = data;
      });
    }

    function loadPossibleSecretPines() {
      WorkerService.all( function(data) {
        $scope.posibleSecretPines = data;
      });
    }

    }
  )
