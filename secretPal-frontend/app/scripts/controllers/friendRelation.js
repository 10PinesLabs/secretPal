'use strict';

var app = angular.module('secretPalApp');

function randomFrom(possibleRecievers) {
  return possibleRecievers[Math.floor(Math.random() * possibleRecievers.length)];
}

app.controller('FriendRelationController', function ($scope, $modal, $filter, FriendRelationService, SweetAlert) {

  updateAllRelations();


  function updatePosibilities() {
    FriendRelationService.allPosibleRelations(function (data) {
      $scope.posibleRelations = data;
    });
  }

  function updateInmutableRelations() {
    FriendRelationService.allInmutableRelations(function (data) {
      $scope.inmutableRelations = data;
    });
  }

  function updateExistingRelations() {
    FriendRelationService.all(function (data) {
      $scope.friendRelations = data;
      $scope.posibilities = $scope.friendRelations.map(function (relation) {
        return relation.giftGiver;
      });
    });
  }

  function updateAllRelations() {
    updatePosibilities();
    updateInmutableRelations();
    updateExistingRelations();
  }

  $scope.autoAssignPine = function (receiver, possibleGivers) {
    SweetAlert.swal({
      title: "Actualizando",
      text: "Esto puede tardar un rato...\n muchos algoritmos",
      showConfirmButton: false,
      timer: 500
    }, function () {
      $scope.update(randomFrom(possibleGivers), receiver);
    });
  };

  $scope.deleteRelation = function (relation) {
    FriendRelationService.delete(relation.giftGiver.id, relation.giftReceiver.id, function () {
      $scope.friendRelations = $filter('filter')($scope.friendRelations, {giftGiver: '!' + relation.giftGiver});
    });
  };

  $scope.update = function (giver, receiver) {
    FriendRelationService.update(giver, receiver, function () {
      updateAllRelations();
      updatePossibleRelation(receiver);
    });
  };

  $scope.delete = function (giver) {
    SweetAlert.swal({
        title: "¿Estás seguro?",
        text: giver.fullName + " no será amigo invisible de nadie!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d43f3a",
        confirmButtonText: "Si, borrar!",
        closeOnConfirm: false
      },
      function (isConfirm) {
        if (isConfirm) {
          FriendRelationService.delete(giver, function () {
            updatePosibilities();
            SweetAlert.swal("Relación eliminada exitosamente", "Ahora " + giver.fullName + " no es amigo invisible de nadie ", "success");
          });
        }
      });
  };

  $scope.lock = function (giver, receiver) {
    var relationToLock = $scope.friendRelations.find(function (relation) {
      return relation.giftGiver.id === giver.id && relation.giftReceiver.id === receiver.id;
    });
    if (typeof relationToLock !== "undefined") {
      FriendRelationService.lock(relationToLock, updateAllRelations);
    } else {
      SweetAlert.swal("Algo salió mal", "No se pudo encontrar una relación entre esos pinos", "error");
    }
  };

  $scope.noPossibilities = function (posRelation) {
    return posRelation.possibleGivers.length === 0;
  };

  function updatePossibleRelation(receiver) {
    FriendRelationService.updatePosibleRelation(receiver.id, function (data) {
      $scope.posibleRelations = $scope.posibleRelations.map(function (pr) {
        if (pr.receiver === receiver) {
          return data;
        }
        else {
          return pr
        }
      })
    });
  }

  $scope.hasSecretPal = function (relation) {
    return relation.possibleGivers.length === 1 && relation.possibleGivers !== null;
  };

  function haveToUpdate(relation) {
    return $scope.noPossibilities(relation) || $scope.hasSecretPal(relation);
  }

  $scope.updateRelation = function (relation) {
    if (haveToUpdate(relation)) {
      updatePossibleRelation(relation.receiver);
    }
  };


})
;
