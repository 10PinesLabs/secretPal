'use strict';

angular.module('secretPalApp')
  .controller('GuessesController', function ($scope, $http, user, GuessesService, WorkerService) {
      $scope.hints = [];
      $scope.guess = null;
      $scope.user = user;
      $scope.today = new Date();
      $scope.maxGuesses = 0;
      $scope.attemptsDone = [];
      $scope.hasGuessedCorrectly = false;
      $scope.secretPine = "";

      $scope.canKeepPlaying = function () {
        return $scope.attemptsDone.length < $scope.maxGuesses;
      };

      $scope.attemptsLeft = function () {
        var lifesLeft = $scope.maxGuesses - $scope.attemptsDone.length;
        return Array.from(new Array(lifesLeft).keys());
      };

      $scope.guessSecretPine = function () {
        GuessesService.makeGuess(user, $scope.guess.fullName, loadGuessStatus);
      };

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
        GuessesService.maxGuesses(function (data) {
          $scope.maxGuesses = data;
        });
      }

      function loadGuessStatus() {
        GuessesService.currentStatus(user, function (data) {
          $scope.attemptsDone = data.guessAttempts;
          $scope.hasGuessedCorrectly = data.wasGuessed;

          loadPossibleSecretPines();
          getSecretPine();
        });
      }

      function loadHints() {
        GuessesService.getHints(user, function (data) {
          $scope.hints = data;
        });
      }

    function getSecretPine() {
      GuessesService.getSecretPine(user, function (data) {
        $scope.secretPine = data;
      });
    }

      function loadPossibleSecretPines() {
        WorkerService.all(function (data) {
          function isSelectable(pine) {
            return !($scope.attemptsDone.includes(pine.fullName) || pine.fullName === user.worker.fullName);
          }
          $scope.posibleSecretPines = data.filter(isSelectable);
        });
      }


    loadGuessStatus();
    loadMaxGuesses();
    loadHints();

    }
  );
