'use strict';

angular.module('secretPalApp')
  .controller('ProfileController', function ($scope, $http, user, $location, FriendRelationService, WishlistService, SweetAlert, WorkerService) {

    $scope.wishlist = [];
    $scope.giftDefault;


    $scope.haveFriend = function() {
      return $scope.friend !== undefined;
    };

    $scope.wantToParticipateMsg = function () {
      SweetAlert.swal({
          title: "No estás participando",
          text: "Queres participar?",
          type: "warning",
          allowOutsideClick: false,
          showConfirmButton: true,
          showCancelButton: true,
          closeOnConfirm: false,
          closeOnCancel: true,
          confirmButtonText: "¡Si!",
          confirmButtonColor: "#68bd46",
          cancelButtonText: "Cancelar",
          cancelButtonColor: '#FFFFFF',
        },
        function (isConfirm) {
          if (isConfirm) {
            WorkerService.changeIntention(user.worker);
            SweetAlert.swal("¡Ahora estás participando!");
            $scope.noFriendAlert();
          } else {
            $location.path('/');
          }
        });
    };
    if (!user.worker.wantsToParticipate) {
      $scope.wantToParticipateMsg();
    } else {
      FriendRelationService.getFriend(user.worker, function (friend) {

        $http.get('/api/auth/giftsDefault').success(function (data) {
          $scope.giftDefault = data.giftDefault;
          $scope.amountDefault = data.amountDefault;
        }).error(function () {
          errorMsg("No se han podido cargar el regalo y el monto default. Por favor intentelo de nuevo msá tarde.");
        });

        $scope.friend = friend;

        WishlistService.getAllWishesFor($scope.friend.data, function (wishlistResponse) {
          $scope.wishlist = wishlistResponse.data;
        });
      });
    }
  });
