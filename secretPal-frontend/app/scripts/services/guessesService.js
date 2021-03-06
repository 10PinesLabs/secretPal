'use strict';


angular.module('secretPalApp').service('GuessesService', function ($http, SweetAlert) {

  function buildRoute(path) {
    var route = '/api/friendRelation';
    return route + path;
  }

  function errorMsg(msg) {
    SweetAlert.swal("Algo salio mal", msg, "error");
  }

  this.currentStatus = function (user, callback) {
    $http.get(buildRoute('/guessFor/' + user.worker.id)).success(function (data) {
      callback(data);
    }).error(function () {
    });
  };

  this.getHints = function (user, callback) {
    $http.get(buildRoute('/hintsFor/' + user.worker.id)).success(function (data) {
      callback(data);
    }).error(function () {
        callback([]);
      }
    )
  };

  this.makeGuess = function (user, guessFullName, callback) {
    $http.put(buildRoute('/guessFor/' + user.worker.id), guessFullName).success(function (data) {
      callback(data);
    }).error(function () {
      errorMsg("No se pudo realizar la adivinanza, inténtelo de nuevo más tarde.");
    });
  };

  this.getMaxGuesses = function (callback) {
    $http.get(buildRoute('/guessLimit')).success(function (data) {
      callback(data);
    }).error(function () {
      errorMsg("No limit found");
    });
  };


});
