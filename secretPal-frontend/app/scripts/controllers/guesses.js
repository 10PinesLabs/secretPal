'use strict';

angular.module('secretPalApp')
  .controller('GuessesController', function ($scope, $http, user, GuessesService, $modal, $log, SweetAlert) {
      $scope.hints = [];

      GuessesService.all(user, function (data) {
        $scope.hints = data;
      });

    }
  )
    .service('GuessesService', function ($http, SweetAlert) {

      function buildRoute(path) {
        var route = '/api/friendRelation';
        return route + path;
      }

      function errorMsg(msg) {
        SweetAlert.swal("Algo salio mal", msg, "error");
      }

      this.all = function (user, callback) {
        $http.get(buildRoute('/hintsFor/' + user.worker.id)).success(function (data) {
          callback(data);
        }).error(function () {
          errorMsg("No se pudo cargar la lista de pistas  , inténtlo de nuevo más tarde.");
        });
      };


    })
