'use strict';

angular.module('secretPalApp')
  .controller('GuessesController', function ($scope, $http, user, GuessesService, WorkerService, SweetAlert) {
      $scope.hints = [];
      $scope.guess = null;
      $scope.user = user;
      $scope.today = new Date();
      $scope.maxGuesses = 0;
      $scope.attemptsDone = 0;
      $scope.hasGuessedCorrectly = false;

    $scope.canKeepPlaying = function () {
      return $scope.attemptsDone < $scope.maxGuesses;
    };

    $scope.attempts = function (number) {
      return Array.from(new Array(number).keys());
    };

    $scope.guessSecretPine = function () {
      SweetAlert.swal({
          title: "¿Estás seguro?",
          text: " Si te quedas sin vidas, perdes",
          type: "warning",
          showCancelButton: true,
          confirmButtonColor: "#32d48a",
          confirmButtonText: "Si, arriesgar!",
          closeOnConfirm: false
        },
        function (isConfirm) {
          if (isConfirm) {
            GuessesService.makeGuess(user, $scope.guess.fullName, function(response){
              if(response.wasGuessed){
                SweetAlert.swal("Adivinaste!", "", "success");
              }else{
                SweetAlert.swal("Te equivocaste", "Perdiste una vida ", "error");
              }
              $scope.attemptsDone = response.guessAttempts;
              $scope.hasGuessedCorrectly= response.wasGuessed;
            });
          }
        });

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

    loadMaxGuesses();
    loadGuessStatus();
    loadHints();
    loadPossibleSecretPines();

    }
  );
