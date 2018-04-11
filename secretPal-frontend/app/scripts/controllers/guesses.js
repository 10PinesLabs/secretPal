'use strict';

angular.module('secretPalApp')
  .controller('GuessesController', function ($scope, $http, user, GuessesService, WorkerService) {
      $scope.hints = [];
      $scope.guess = null;
      $scope.user = user;
      $scope.today = new Date();
      $scope.maxGuesses = 3;
      $scope.remainingAttempts = 2;
      $scope.hasGuessedCorrectly = false

    loadMaxGuesses();
    loadGuessStatus();
    loadHints();
    loadPossibleSecretPines();

    $scope.attempts = function (number) {
      return Array.from(Array(number).keys());
    }

    $scope.guessSecretPine = function () {
      GuessesService.makeGuess(user, $scope.guess.fullName, function(response){
        $scope.remainingAttempts = response.remainingGuessAttempts;
        $scope.hasGuessedCorrectly= response.wasGuessed;
      });
    }

    $scope.diff = function (date) {
      var unDia = 24 * 60 * 60 * 1000; // hora*minuto*segundo*milli
      var birthday = new Date(date);
      birthday.setYear($scope.today.getFullYear());

      return Math.round((birthday.getTime() - $scope.today.getTime()) / unDia);
    };

    $scope.afterBirthday = function () {
      var date = $scope.user.worker.dateOfBirth;
      var diff = $scope.diff(date);

      return (diff < 0);
    };

    function loadMaxGuesses() {
      GuessesService.maxGuesses(user, function (data) {
        $scope.maxGuesses = data;
      });
    }

    function loadGuessStatus() {
      GuessesService.currentStatus(user, function (data) {
        $scope.remainingAttempts = data.remainingGuessAttempts;
        $scope.hasGuessedCorrectly = data.wasGuessed;
      });
    }

    function loadHints() {
      GuessesService.getHints(user, function (data) {
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
