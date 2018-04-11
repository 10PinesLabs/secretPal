'use strict';

angular.module('secretPalApp')
  .controller('HintsController', function ($scope, $http, user, HintsService, $modal, $log, SweetAlert) {
      $scope.hints = [];
      $scope.limit = 3;

      HintsService.limit(function (limit) {
        $scope.limit = limit;
      });

      HintsService.all(user, function (data) {
        $scope.hints = data;
      });

      $scope.canBeAdded= function () {
        return $scope.hints.length < $scope.limit
      }

      $scope.add = function () {

        HintsService.new(user, $scope.hint, function (hint) {

          $scope.hints.push(hint);
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
