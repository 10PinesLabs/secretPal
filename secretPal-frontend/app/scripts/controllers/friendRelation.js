'use strict';

var app = angular.module('secretPalApp');

function randomFrom(possibleRecievers) {
  return possibleRecievers[Math.floor(Math.random() * possibleRecievers.length)];
}

app.controller('FriendRelationController', function ($scope, $modal, $filter, FriendRelationService, SweetAlert) {

  function updatePosibilities() {
    FriendRelationService.allPosibleRelations( function(data) {
      $scope.posibleRelations = data;
    });
  }

  updatePosibilities();

  function updateInmutableRelations() {
    FriendRelationService.allInmutableRelations( function(data) {
      $scope.inmutableRelations = data;
    });
  }

  updateInmutableRelations();

  $scope.today = new Date();
  $scope.alreadySelected = {};

  $scope.hasBirthdayInAMonth = function (worker) {
    return new Date(worker.dateOfBirth).getMonth() <= $scope.thisMonth;
  };

  $scope.diff = function (date) {
    var unDia = 24 * 60 * 60 * 1000; // hora*minuto*segundo*milli
    var birthday = new Date(date);
    birthday.setYear($scope.today.getFullYear());

    return Math.round((birthday.getTime() - $scope.today.getTime()) / unDia);
  };

  $scope.hasBirthdayPassed = function (relation) {
    var date = relation.giftReceiver.dateOfBirth;
    var diff = $scope.diff(date);

    return (diff < 0);
  };

  $scope.birthdayHasNotPassed = function (relation) {
    var date = relation.giftReceiver.dateOfBirth;
    var diff = $scope.diff(date);

    return (diff > 0);
  };


  $scope.birthdayPassOnAMonth = function (relation) {
    var date = relation.giftReceiver.dateOfBirth;
    var diff = $scope.diff(date);

    return (diff < 30);
  };

  $scope.dayDifference = function (date) {
    var diff = $scope.diff(date);
    if (diff > 0) {
      return "Faltan solo " + diff + " dias";
    }
  };

  FriendRelationService.all(function (data) {
    $scope.friendRelations = data;
    $scope.posibilities = $scope.friendRelations.map(function (relation) {

      $scope.alreadySelected[relation.giftGiver.fullName] = $scope.notNull(relation);

      return relation.giftGiver;
    });

    console.log($scope.posibilities);

    $scope.relations = $filter('filter')($scope.friendRelations, {giftReceiver: null});
  });

  $scope.deleteRelation = function (relation) {
    FriendRelationService.delete(relation.giftGiver.id, relation.giftReceiver.id, function () {
      $scope.friendRelations = $filter('filter')($scope.friendRelations, {giftGiver: '!' + relation.giftGiver});
    });
  };

  $scope.notNull = function (relation) {
    return (relation.giftReceiver !== null);
  };

  $scope.notUsed = function (giftGiverSelected) {
    return function (relation) {
      var notUsed = true;
      angular.forEach($scope.friendRelations, function (fr) {
        if (fr.giftReceiver !== null) {
          if (fr.giftGiver.id !== giftGiverSelected) {
            notUsed = false;
          }

        }
      });
      return notUsed;
    };
  };

  $scope.validFriendRelations = function (fr) {
    return $scope.friendRelations.filter(function (elem) {
      if ($scope.notUsed(fr.giftGiver)) {
        return fr.giftGiver.id !== elem.giftReceiver.id && fr.giftGiver.id !== elem.giftGiver.id;
      }


    });
  };

  $scope.auto = function(){
    // SweetAlert.swal({
    //   title: "Actualizando",
    //   text: "Esto puede tardar un rato...\n muchos algoritmos",
    //   showConfirmButton: false
    // });

    FriendRelationService.autoAssign(updatePosibilities);

  };

  $scope.autoSelected = function(worker){
    return $scope.alreadySelected[worker.fullName];
  };

  $scope.removeAutoSelect = function(worker){
    $scope.toggleAlreadySelected(worker, false);
  };

  $scope.toggleAlreadySelected = function(worker, boolean){
    $scope.alreadySelected[worker.fullName] = boolean;
  };

  $scope.autoAssignPine = function(giver, possibleRecievers) {
    // SweetAlert.swal({
    //   title: "Actualizando",
    //   text: "Esto puede tardar un rato...\n muchos algoritmos",
    //   showConfirmButton: false
    // });
    $scope.update(giver, randomFrom(possibleRecievers));
  };

  $scope.update = function(giver, receiver) {
    $scope.toggleAlreadySelected(giver, true);
    FriendRelationService.update(giver, receiver, updatePosibilities);
  };

  $scope.delete = function (giver) {
    // SweetAlert.swal({
    //     title: "¿Estás seguro?",
    //     text:  giver.fullName + " no será amigo invisible de nadie!",
    //     type: "warning",
    //     showCancelButton: true,
    //     confirmButtonColor: "#d43f3a",
    //     confirmButtonText: "Si, borrar!",
    //     closeOnConfirm: false
    //   },
    //   function (isConfirm) {
    //     if (isConfirm) {
          FriendRelationService.delete(giver, function () {
            updatePosibilities();
            $scope.toggleAlreadySelected(giver, false);
            SweetAlert.swal("Relación eliminada exitosamente", "Ahora " + giver.fullName + " no es amigo invisible de nadie ", "success");
          });
        }
  //     });
  // };

});
