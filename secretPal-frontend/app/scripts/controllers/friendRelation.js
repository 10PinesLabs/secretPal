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
    }, function() {
      $scope.update(randomFrom(possibleGivers),receiver);
    });
  };

  $scope.deleteRelation = function (relation) {
    FriendRelationService.delete(relation.giftGiver.id, relation.giftReceiver.id, function () {
      $scope.friendRelations = $filter('filter')($scope.friendRelations, {giftGiver: '!' + relation.giftGiver});
    });
  };

  $scope.update = function (giver, receiver) {
    FriendRelationService.update(giver, receiver, updateAllRelations);
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

  $scope.relationIsFromGiverToReceiver = function (relation, giver, receiver) {
    return relation.giftGiver.id === giver.id && relation.giftReceiver.id === receiver.id;
  }

  $scope.lock = function (giver, receiver) {
    SweetAlert.swal({
        title: "¿Estás seguro?",
        text: "Una vez que se fije la relación se enviará un mail avisando a quien tiene que comprar el regalo",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d43f3a",
        confirmButtonText: "Si, fijar!",
        closeOnConfirm: false
      },
      function (isConfirm) {
        if (isConfirm) {
          var relationToLock = $scope.friendRelations.find(function (relation){
            return $scope.relationIsFromGiverToReceiver(relation, giver, receiver);
          });
          if (typeof relationToLock !== "undefined") {
            SweetAlert.swal({
              title: "Procesando",
              text: "Esto puede tardar un rato...\n muchos algoritmos",
              showConfirmButton: false
            });
            FriendRelationService.lock(relationToLock, updateAllRelations);
          } else {
            SweetAlert.swal("Algo salió mal", "No se pudo encontrar una relación entre esos pinos", "error");
          }
        }
      });
  }
});
