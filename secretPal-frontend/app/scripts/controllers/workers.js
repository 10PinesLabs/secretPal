'use strict';

var app = angular.module('secretPalApp');
app.controller('WorkersController', function($scope, $modal, $rootScope, WorkerService, FriendRelationService, $filter, $location, user, SweetAlert) {

    function warningMsg(msg){
      SweetAlert.swal({
       title: "",
       text: msg,
       type: "warning"
      });
    }

    /*TODO: Sacar antes de pushesr
    if( !user.data.admin ){
      $location.path("/");
    }*/

    WorkerService.all(function(data){ $scope.workers = data;});
    FriendRelationService.all( function(data) {$scope.participants = data;});

    $scope.deleteWithConfirmationMSg = function(worker) {
      SweetAlert.swal({
          title: "Estas seguro?",
          text: "No vas a poder recuperar este pino!",
          type: "warning",
          showCancelButton: true,
          confirmButtonColor: "#DD6B55",
          confirmButtonText: "Si, borrar!",
          closeOnConfirm: false
        },
        function (isConfirm) {
          if (isConfirm) {
            WorkerService.delete(worker.id, function() {
              $scope.workers = $filter('filter')($scope.workers, {id: '!' + worker.id});
              SweetAlert.swal("Se ha borrado exitosamente");
            });
          }
        });
    };

    $scope.delete = function (worker) {
      if (worker.wantsToParticipate) {
        warningMsg("Este trabajador esta participando. No se puede borrar.");
      } else {
        $scope.deleteWithConfirmationMSg(worker);
      }
    };

    $scope.Reset = function () {
        $scope.newName = '';
        $scope.newMail = '';
        $scope.newDate = '';
    };

    $scope.Add = function () {
      var newWorker = buildWorker();
      WorkerService.new(newWorker, function(persistedWorker) {
        $scope.workers.push(persistedWorker);
      });
      $scope.Reset();
    };

    function buildWorker() {
      return {fullName: $scope.newName, eMail: $scope.newMail, dateOfBirth: $scope.newDate,  wantsToParticipate: false};
    }

    $scope.changeIntention = function (worker) {
      var keepGoing = true;
      angular.forEach($scope.participants, function(participant) {
          if (keepGoing){
            if (worker.id === participant.giftGiver.id && participant.giftReceiver !== null ) {
              warningMsg("Este trabajador es el amigo invisible de otro participante. Se debe borrar esa relacion antes de que deje de participar.");
              worker.wantsToParticipate = true;
              keepGoing = false;
            }
            if (participant.giftReceiver !== null && worker.id === participant.giftReceiver.id) {
              warningMsg("Hay un participante que es amigo invisible de este trabajador.Se debe borrar esa relacion antes de que deje de participar.");
              worker.wantsToParticipate = true;
              keepGoing = false;
            }
          }
        }
      );
      if(keepGoing) {WorkerService.changeIntention(worker);}

    };

    $scope.assignation = function() {
      $location.path('/friendRelations');
    };

    /*DATEPICKER FUNCTIONS*/
    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope.opened = true;
    };

});

app.directive('unique', function() {
  return {
    require: 'ngModel',
    restrict: 'A',
    link: function(scope, elm, attrs, ctrl) {
      ctrl.$validators.unique = function(modelValue) {
        if (ctrl.$isEmpty(modelValue)) {
          return true;
        }

        var result = [];

        angular.forEach(scope.workers, function(worker){
          result.push( worker.name.toUpperCase() );
        });

        return result.indexOf(modelValue.toUpperCase()) === -1;
      };
    }
  };
});
