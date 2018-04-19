'use strict';

var app = angular.module('secretPalApp');

function randomFrom(possibleRecievers) {
  return possibleRecievers[Math.floor(Math.random() * possibleRecievers.length)];
}

app.controller('FriendRelationController', function ($scope, $modal, $filter, FriendRelationService, SweetAlert) {

  function updatePosibilities() {
    FriendRelationService.allPosibleRelations(function (data) {
      $scope.posibleRelations = data;
    });
  }

  updatePosibilities();

  function updateInmutableRelations() {
    FriendRelationService.allInmutableRelations(function (data) {
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
    $scope.friendRelations.forEach(function (relation) {
      $scope.alreadySelected[relation.giftReceiver.fullName] = $scope.notNull(relation);
    });
  });

  $scope.deleteRelation = function (relation) {
    FriendRelationService.delete(relation.giftGiver.id, relation.giftReceiver.id, function () {
      $scope.friendRelations = $filter('filter')($scope.friendRelations, {giftGiver: '!' + relation.giftGiver});
    });
  };

  $scope.notNull = function (relation) {
    return (relation.giftReceiver !== null);
  };

  $scope.autoSelected = function (worker) {
    return $scope.alreadySelected[worker.fullName];
  };

  $scope.removeAutoSelect = function (worker) {
    $scope.toggleAlreadySelected(worker, false);
  };

  $scope.toggleAlreadySelected = function (worker, boolean) {
    $scope.alreadySelected[worker.fullName] = boolean;
  };

  $scope.autoAssignPine = function (receiver, possibleGivers) {
    $scope.update(randomFrom(possibleGivers),receiver);
  };

  $scope.update = function (giver, receiver) {
    $scope.toggleAlreadySelected(receiver, true);
    FriendRelationService.update(giver, receiver, updatePosibilities);
  };

  $scope.delete = function (giver) {
    FriendRelationService.delete(giver, function () {
      updatePosibilities();
      $scope.toggleAlreadySelected(giver, false);
      SweetAlert.swal("Relación eliminada exitosamente", "Ahora " + giver.fullName + " no es amigo invisible de nadie ", "success");
    });
  }

});
