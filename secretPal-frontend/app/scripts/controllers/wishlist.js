'use strict';

angular.module('secretPalApp')
  .controller('WishlistController', function ($scope, user, WorkerService, WishlistService, $modal, $log, SweetAlert) {

    WishlistService.all(function (data) {
      $scope.wishlist = data;
    });

    WorkerService.all(function (data) {
      $scope.posibleWorkers = data;
    });

    $scope.Add = function () {
      $scope.wish.createdBy = user.worker;
      WishlistService.new($scope.wish, function (persistedWish) {
        $scope.wishlist.push(persistedWish);
        $scope.Reset();
      });
    };

    $scope.Reset = function () {
      $scope.wish.worker = null;
      $scope.wish.gift = '';
    };

    $scope.Edit = function (wish) {
      var modalInstance = $modal.open({
        animation: false,
        templateUrl: 'editModalWish.html',
        controller: 'ModalInstanceCtrl',
        resolve: {
          wish: function () {
            return angular.copy(wish);
          }
        }
      });
      modalInstance.result.then(function (returnedWish) {
        angular.copy(returnedWish, wish);
        WishlistService.update(wish);
      });
    };

    $scope.Delete = function (wish) {
      SweetAlert.swal({
          title: "¿Estás seguro?",
          text: "¡No vas a poder recuperar este deseo!",
          type: "warning",
          allowOutsideClick: false,
          showConfirmButton: true,
          showCancelButton: true,
          closeOnConfirm: false,
          closeOnCancel: true,
          confirmButtonColor: "#d43f3a",
          confirmButtonText: "Si, borrar!",
          cancelButtonText: "Cancelar"
        },
        function (isConfirm) {
          if (isConfirm) {
            WishlistService.delete(wish, function () {
              $scope.wishlist.splice(
                $scope.wishlist.indexOf(wish), 1
              );
              SweetAlert.swal({
                title: "Regalo borrado exitosamente",
                confirmButtonColor: "#68bd46",
              });
            });
          }
        });
    };

    $scope.canDelete = function (wish) {
      return user.worker.id == wish.createdBy.id || user.worker.id == wish.worker.id;
    };
  })
  .controller('ModalInstanceCtrl', function ($scope, $modalInstance, wish) {
    $scope.wish = wish;
    $scope.ok = function () {
      $modalInstance.close($scope.wish);
    };
    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  })
  .service('WishlistService', function ($http, SweetAlert) {

    function buildRoute(path) {
      var route = '/api/wishlist';
      return route + path;
    }

    function errorMsg(msg) {
      SweetAlert.swal("Algo salio mal", msg, "error");
    }



    this.all = function (callback) {
      $http.get(buildRoute('/')).success(function (data) {
        callback(data);
      }).error(function () {
        errorMsg("No se pudo cargar la lista de regalos, inténtlo de nuevo más tarde.");
      });
    };

    this.new = function (wish, successFunction) {
      $http.post(buildRoute('/'), wish).success(function (data) {
        successFunction(data);
      }).error(function () {
        errorMsg("No se pudo crear un regalo, por favor inténtelo de nuevo.");
      });
    };

    this.delete = function (wish, successFunction) {
      $http.delete(buildRoute('/' + wish.id)).success(function () {
        successFunction();
      });
    };

    this.update = function (wish) {
      $http.post(buildRoute('/') + wish.id, wish.gift);
    };

    this.getAllWishesFor = function (worker, callback) {
      $http.get(buildRoute('/worker/' + worker.id)).then(function (data) {
        callback(data);
      });
    };
  });
