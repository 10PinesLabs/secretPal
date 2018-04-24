'use strict';

angular.module('secretPalApp')
  .controller('GuessesController', function ($scope, $http, user, GuessesService, WorkerService, SweetAlert) {
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
      function makeGuess() {
        GuessesService.makeGuess(user, $scope.guess.fullName, function (response) {
          if (response.wasGuessed) {
            SweetAlert.swal({
              title:"Adivinaste!",
              text: "",
              type: "success",
              showConfirmButton:false,
              timer: 1000
            });
          } else {
            SweetAlert.swal({
              title:"Te equivocaste!",
              text: "Perdiste una vida",
              type: "error",
              showConfirmButton:false,
              timer: 1000
            });
          }
          $scope.attemptsDone = response.guessAttempts;
          $scope.hasGuessedCorrectly = response.wasGuessed;
        });
      }

      var lastChance = $scope.maxGuesses - $scope.attemptsDone === 1;

      if (lastChance) {
        SweetAlert.swal({
            title: "¿Estás seguro?",
            text: "Si te equivocas, perdes.",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#32d48a",
            confirmButtonText: "Si, arriesgar!",
            closeOnConfirm: false
          },
          function (isConfirm) {
            if (isConfirm) {
              makeGuess();
            }
          });
      }
      else{
        makeGuess();
      }

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
        GuessesService.getMaxGuesses(function (data) {
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
            var wasNotAFailedGuess = !$scope.attemptsDone.includes(pine.fullName);
            var isNotMe = !pine.fullName === user.worker.fullName;

            return wasNotAFailedGuess && isNotMe;
          }

          $scope.posibleSecretPines = data.filter(isSelectable);
        });
      }


    loadGuessStatus();
    loadMaxGuesses();
    loadHints();

    }
  );
