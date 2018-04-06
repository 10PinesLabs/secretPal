'use strict';

angular.module('secretPalApp')
  .controller('HintsController', function ($scope, user, WorkerService, HintsService, $modal, $log, SweetAlert) {

    HintsService.all(function (data) {
      $scope.hints = data;
    });

    WorkerService.all(function (data) {
      $scope.posibleWorkers = data;
    });

    $scope.Add = function () {
      $scope.hints.createdBy = user.worker;
      HintsService.new($scope.hint, function (persistedHint) {
        $scope.hints.push(persistedHint);
        $scope.Reset();
      });
    };

    $scope.Reset = function () {
      $scope.hint = null;
    };

    $scope.Edit = function (hint) {
      var modalInstance = $modal.open({
        animation: false,
        templateUrl: 'editModalWish.html',
        controller: 'ModalInstanceCtrl',
        resolve: {
          hint: function () {
            return angular.copy(hint);
          }
        }
      });
      modalInstance.result.then(function (returnedHint) {
        angular.copy(returnedHint, hint);
        HintsService.update(hint, returnedHint);
      });
    };

    $scope.Delete = function (hint) {
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
            HintsService.delete(wish, function () {
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
  .service('HintsService', function ($http, SweetAlert) {

    function buildRoute(path) {
      var route = '/api/friendRelation';
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
    this.update = function (hint, returnedHint) {
      $http.put(buildRoute('/hintsFrom/') + worker.id, hint, returnedHint);
    };
    this.getAllHintsFor = function (worker, callback) {
      $http.get(buildRoute('/hintsFor/' + worker.id)).then(function (data) {
        callback(data);
      });
    };
  });
