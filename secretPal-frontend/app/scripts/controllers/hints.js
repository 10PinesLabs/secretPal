'use strict';

angular.module('secretPalApp')
  .controller('HintsController', function ($scope, $http, user, HintsService, $modal, $log, SweetAlert) {
      $scope.hints = [];

      HintsService.all(user, function (data) {
        $scope.hints = data;
      });

      $scope.canBeAdded= function () {
        return $scope.hints.length < 3
      }

      $scope.add = function () {

        HintsService.new(user, $scope.hint, function () {
          $scope.hints.push({message: $scope.hint});
        });
      };

      $scope.edit = function (hint) {
        var modalInstance = $modal.open({
          animation: false,
          templateUrl: 'editModalHint.html',
          controller: 'ModalInstanceCtrlEdit',
          resolve: {
            user: function () {
              return angular.copy(user);
            },
            hint: function () {
              return angular.copy(hint);
            }
          }
        });
        modalInstance.result.then(function (returnedHint) {
          angular.copy(returnedHint, hint);
          HintsService.update(user,returnedHint.id, returnedHint.message);
        });
      };

      $scope.delete = function (hint) {
        HintsService.delete(user, hint.id, function () {
          $scope.hints.splice(
            $scope.hints.indexOf(hint), 1
          );

        });
      };

    }
  )
  .controller('ModalInstanceCtrlEdit', function ($scope, $modalInstance, user, hint) {
    $scope.user = user;
    $scope.hint = hint;
    $scope.ok = function () {
      $modalInstance.close($scope.hint);
    };
    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  })
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
      $http.post(buildRoute('/hintsFrom/' + user.worker.id), hint).success(function () {
        successFunction();
      }).error(function () {
        errorMsg("No se pudo agregar la pista, por favor inténtelo de nuevo.");
      });
    };

    this.delete = function (user, hint, successFunction) {
      $http.delete(buildRoute('/hintsFrom/' + user.worker.id + "/" + hint)).success(function (data) {
        successFunction(data);
      });
    };

    this.update = function (user, hint, newHint, successFunction) {
      $http.put(buildRoute('/hintsFrom/' + user.worker.id + "/" + hint), newHint).success(function (data) {
        successFunction(data);
      });
    };


  })
