'use strict';

angular.module('secretPalApp')
  .controller('GuessesController', function ($scope, $http, user, GuessesService, WorkerService, SweetAlert) {
      $scope.hints = [];
      $scope.guess = null;

    loadHints();
    loadPossibleSecretPines();

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

    $scope.sendGuess = function () {
      GuessesService.evaluateGuessForUser(guess, user);
    };

    }
  )
