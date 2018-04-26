'use strict';

angular.module('secretPalApp')
  .controller('HintsController', function ($scope, $http, user, HintsService, $modal, FriendRelationService, SweetAlert) {
      $scope.hints = [];
      $scope.limit = 3;

      FriendRelationService.getFriend(user.worker, function (friend) {

        $scope.friend = friend;
        if (friend != null) {
          HintsService.all(user, function (data) {
            $scope.hints = data;
          });
        }
      });

      HintsService.hintsLimit(function (number) {
        $scope.limit = number;
      });

      $scope.hasFriend = function () {
        return $scope.friend != null;
      };

      $scope.canBeAdded = function () {
        return $scope.hints.length < $scope.limit
      };

      $scope.add = function () {

        var newHint = {message: $scope.hintMessage};
        HintsService.new(user, newHint, function (hint) {

          $scope.hints.push(hint);
          $scope.hintMessage = null;
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
          HintsService.update(user, returnedHint.id, returnedHint.message);
        });
      };

      $scope.delete = function (hint) {
        SweetAlert.swal({
            title: "¿Estás seguro?",
            text: "Si borras esta pista, tu pino invisible ya no va a poder verla",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d43f3a",
            confirmButtonText: "Borrar",
            closeOnConfirm: true
          },
          function (isConfirm) {
            if (isConfirm) {
              HintsService.delete(user, hint.id, function () {
                $scope.hints.splice(
                  $scope.hints.indexOf(hint), 1
                );
              });
            }
          });
      };

    }
  )
  .controller('ModalInstanceCtrlEdit', function ($scope, $modalInstance, user, hint) {
    $scope.user = user;
    $scope.hintMessage = hint;
    $scope.ok = function () {
      $modalInstance.close($scope.hintMessage);
    };
    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
