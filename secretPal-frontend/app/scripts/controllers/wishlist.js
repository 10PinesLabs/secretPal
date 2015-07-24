'use strict';

var app = angular.module('secretPalApp')
  .controller('WishlistController', function ($scope, WorkerService, WishlistService, $modal, $log) {

    WishlistService.all(function (data) {
      $scope.wishlist = data;
    });

    WorkerService.all(function (data) {
      $scope.posibleWorkers = data;
    });

    $scope.Add = function () {
      WishlistService.new($scope.wish, function(persistedWish) { $scope.wishlist.push(persistedWish); $scope.Reset();});
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
      }, function () {
        $log.info('Modal dismissed at: ' + new Date());
      });
    };

    $scope.Delete = function (wish) {
      WishlistService.delete(wish, function () {
        $scope.wishlist.splice(
          $scope.wishlist.indexOf(wish), 1
        );
      })
    }

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

  .service('WishlistService', function ($http) {
  function buildRoute(path) {
    var route = 'http://localhost:9090/wishlist';
    return route + path;
  }

  this.all = function(callback) {
    $http.get(buildRoute('/')).
      success(function(data) {
        callback(data);
      }).
      error(function() {
        alert("Something went wrong, try again later.");
      });
  };

    this.new = function(wish, successFunction) {
      $http.post(buildRoute('/'), wish).
        success(function(data) {
          successFunction(data);
        }).
        error(function() {
          alert("Something went wrong, try again later.");
        });
    };

    this.delete = function (wish, successFunction) {
      $http.delete(buildRoute('/' + wish.id)).
      success(function() {
        successFunction();
      });
  };

    this.update = function (wish) {
      $http.post(buildRoute('/') + wish.id, wish.gift);
    }
});