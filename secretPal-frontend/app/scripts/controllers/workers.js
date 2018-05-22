'use strict';

var app = angular.module('secretPalApp');
app.controller('WorkersController', function ($scope, $modal, $rootScope, WorkerService, FriendRelationService, $filter, $location, SweetAlert, Account, user) {
  $scope.admins = [];

  function warningMsg(msg) {
    SweetAlert.swal({
      title: "",
      text: msg,
      type: "warning"
    });
  }


  function updateAdmins() {
    Account.getAdmins().then(function (admins) {
      $scope.admins = admins.data.map(function (worker) {
        return worker.id;
      });

    });
  }

  function updateWorkersStatus() {
    WorkerService.all(function (data) {
      $scope.workers = data;

      updateAdmins();
    });
  }

  updateWorkersStatus();


  FriendRelationService.all(function (data) {
    $scope.existingRelations = data;
  });

  $scope.deleteWithConfirmationMSg = function (worker) {
    SweetAlert.swal({
        title: "¿Estás seguro?",
        text: "¡No vas a poder recuperar este pino!",
        type: "warning",
        allowOutsideClick: false,
        showConfirmButton: true,
        showCancelButton: true,
        closeOnConfirm: false,
        closeOnCancel: true,
        confirmButtonText: "Si, ¡borrar!",
        confirmButtonColor: "#d43f3a",
        cancelButtonText: "Cancelar",
        cancelButtonColor: '#FFFFFF'
      },
      function (isConfirm) {
        if (isConfirm) {
          WorkerService.delete(worker.id, function () {
            $scope.workers = $filter('filter')($scope.workers, {id: '!' + worker.id});
            SweetAlert.swal({
              title: "Pino borrado exitosamente",
              confirmButtonColor: "#68bd46"
            });
          });
        }
      });
  };

  $scope.delete = function (worker) {
    if (worker.wantsToParticipate) {
      warningMsg("Este pino está participando. No se puede borrar.");
    } else if ($scope.isAnAdmin(worker)) {
      warningMsg("Este pino es administrador. No se puede borrar.");
    } else {
      $scope.deleteWithConfirmationMSg(worker);
    }
  };

  $scope.Reset = function () {
    $scope.newName = '';
    $scope.newNickname = '';
    $scope.newMail = '';
    $scope.newDate = '';
  };

  $scope.Add = function () {
    var newWorker = buildWorker();
    WorkerService.new(newWorker, function (persistedWorker) {
      $scope.workers.push(persistedWorker);
    });
    $scope.Reset();
  };

  $scope.isAnAdmin = function (worker) {

    return $scope.admins.includes(worker.id);
  };

  function buildWorker() {
    return {
      fullName: $scope.newName,
      nickname: $scope.newNickname,
      eMail: $scope.newMail,
      dateOfBirth: $scope.newDate,
      wantsToParticipate: {
        wantsToGive: false,
        wantsToReceive: false,
        wantsToReceiveMail: true
      }
    };
  }


  $scope.addAdmin = function (worker) {
    SweetAlert.swal({
        title: "Atento!",
        text: worker.fullName + " va a ser admin! \n Esto no se puede deshacer",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d43f3a",
        confirmButtonText: "Hacer admin",
        closeOnConfirm: true
      },
      function (isConfirm) {
        if (isConfirm) {
          Account.makeAdmin(worker, updateAdmins);
        }
      }
    );
  };


  $scope.Edit = function (worker) {
    var modalInstance = $modal.open({
      animation: false,
      templateUrl: 'editModalWorker.html',
      controller: 'ModalEditWorkerCtrl',
      resolve: {
        worker: function () {
          return angular.copy(worker);
        }
      }
    });
    modalInstance.result.then(function (returnedWorker) {
      angular.copy(returnedWorker, worker);
      WorkerService.update(worker);
    });
  };

  $scope.updateGifUrlFor = function (worker) {
    var modalInstance = $modal.open({
      animation: false,
      templateUrl: 'updateGifUrlForWorker.html',
      controller: 'WorkerGifCtrl',
      resolve: {
        worker: function () {
          return angular.copy(worker);
        }
      }
    });
    modalInstance.result.then(function (returnedWorker) {
      angular.copy(returnedWorker, worker);
      WorkerService.updateGifUrlFor(worker);
    });
  };

  $scope.showGifFor = function (worker) {
    $modal.open({
      animation: false,
      templateUrl: 'gifViewer.html',
      controller: 'GifViewerCtrl',
      resolve: {
        worker: function () {
          return angular.copy(worker);
        }
      }
    });
  };

  $scope.editParticipation = function (worker) {
    $modal.open({
      animation: false,
      templateUrl: 'customizeWorkerParticipation.html',
      controller: 'ParticipationCtrl',
      resolve: {
        worker: function () {
          return angular.copy(worker);
        },
        existingRelations: function () {
          return angular.copy($scope.existingRelations);
        }
      }
    });
  };

  /*DATEPICKER FUNCTIONS*/
  $scope.open = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope.opened = true;
  };

  $scope.cantParticipate = function (worker) {
    var today = new Date();
    var actualBirthday = new Date(worker.dateOfBirth);
    actualBirthday.setYear(today.getFullYear());

    return actualBirthday <= today;
  };

})
;

app.directive('unique', function () {
  return {
    require: 'ngModel',
    restrict: 'A',
    link: function (scope, elm, attrs, ctrl) {
      ctrl.$validators.unique = function (modelValue) {
        if (ctrl.$isEmpty(modelValue)) {
          return true;
        }

        var result = [];

        angular.forEach(scope.workers, function (worker) {
          result.push(worker.name.toUpperCase());
        });

        return result.indexOf(modelValue.toUpperCase()) === -1;
      };
    }
  };
})

  .controller('ModalEditWorkerCtrl', function ($scope, $modalInstance, worker) {

    $scope.worker = worker;
    $scope.ok = function () {
      $modalInstance.close($scope.worker);
    };
    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    /*DATEPICKER FUNCTIONS*/
    $scope.open = function ($event) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope.opened = true;
    };
  })

  .controller('WorkerGifCtrl', function ($scope, $modalInstance, worker) {
    $scope.worker = worker;
    $scope.ok = function () {
      $modalInstance.close($scope.worker);
    };
    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  })

  .controller('GifViewerCtrl', function ($scope, $modalInstance, worker) {
    $scope.worker = worker;
    $scope.cerrar = function () {
      $modalInstance.dismiss('cancel');
    };
  })
  .controller('ParticipationCtrl', function ($scope, $modalInstance, worker, WorkerService, existingRelations, SweetAlert) {
    $scope.worker = worker;
    $scope.existingRelations = existingRelations;

    function warningMsg(msg) {
      SweetAlert.swal({
        title: "",
        text: msg,
        type: "warning"
      });
    }
    $scope.changeGiftingIntention = function (worker) {
      var keepGoing = true;
      angular.forEach($scope.existingRelations, function (relation) {
          if (keepGoing) {
            if (worker.id === relation.giftGiver.id && relation.giftReceiver !== null) {
              warningMsg("Este trabajador es el amigo invisible de otro participante. Se debe borrar esa relación antes de que deje de participar.");
              worker.wantsToParticipate.wantsToGive = true;
              keepGoing = false;
            }
          }
        }
      );

    };

    $scope.changeReceivingIntention = function (worker) {
      var keepGoing = true;
      angular.forEach($scope.existingRelations, function (relation) {
          if (keepGoing) {
            if (relation.giftReceiver !== null && worker.id === relation.giftReceiver.id) {
              warningMsg("Hay un participante que es amigo invisible de este trabajador.Se debe borrar esa relación antes de que deje de participar.");
              worker.wantsToParticipate.wantsToReceive = true;
              keepGoing = false;
            }
          }
        }
      );
    };

    $scope.ok = function () {
      $modalInstance.close($scope.worker);
      WorkerService.changeIntention($scope.worker);
    };
    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
