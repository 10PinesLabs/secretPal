'use strict';

angular.module('secretPalApp')
  .controller('HintsController', function ($scope, $http, user, HintsService, $modal, $log, SweetAlert) {
      $scope.hints = [];

      HintsService.all(user, function (data) {
        $scope.hints = data;
      });

      $scope.add = function () {

        HintsService.new(user, $scope.hint, function (persistedHint) {
          $scope.hints.push(persistedHint);
        });
      };

      $scope.edit = function (hint) {
        var modalInstance = $modal.open({
          animation: false,
          templateUrl: 'editModalHint.html',
          controller: 'ModalInstanceCtrl',
          resolve: {
            hint: function () {
              return angular.copy(hint);
            }
          }
        });
        modalInstance.result.then(function (returnedHint) {
          angular.copy(returnedHint, hint);
          HintsService.update(hint);
        });
      };

      $scope.delete = function (hint) {
        HintsService.delete(user, $scope.hints.indexOf(hint), function () {
          $scope.hints.splice(
            $scope.hints.indexOf(hint), 1
          );

        });
      };

    }
  )
    .service('HintsService', function ($http, SweetAlert) {

      function buildRoute(path) {
        var route = '/api/friendRelation';
        return route + path;
      }

      function errorMsg(msg) {
        SweetAlert.swal("Algo salio mal", msg, "error");
      }

      this.all = function (user, callback) {
        $http.get(buildRoute('/hintsFrom/' + user.worker.id)).success(function (data) {
          callback(data);
        }).error(function () {
          errorMsg("No se pudo cargar la lista de pistas  , inténtlo de nuevo más tarde.");
        });
      };

      this.new = function (user, hint, successFunction) {
        $http.post(buildRoute('/hintsFrom/' + user.worker.id), hint).success(function (data) {
          successFunction(data);
        }).error(function () {
          errorMsg("No se pudo agregar la pista, por favor inténtelo de nuevo.");
        });
      };

      this.delete = function (user, hint, successFunction) {
        $http.delete(buildRoute('/hintsFrom/' + user.worker.id), {data: hint}).success(function (data) {
          successFunction(data);
        });
      };

    })
