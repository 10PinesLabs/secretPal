'use strict';

var app = angular.module('secretPalApp');
app.controller('FriendRelationController', function ($scope, $modal, $filter, FriendRelationService, SweetAlert) {

  $scope.today = new Date();

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


  $scope.ok = function () {
    SweetAlert.swal({
      title: "Actualizando",
      text: "Esto puede tardar un rato...\n muchos algoritmos",
      showConfirmButton: false
    });
    FriendRelationService.new($scope.friendRelations);
  };

  $scope.auto = function () {
    shuffleArray($scope.posibilities);
    console.log($scope.posibilities.map(function (worker) {
      return worker.fullName;
    }));
    $scope.posibilities.forEach(function (worker, index) {
      $scope.friendRelations.find(function (relation) {
        return relation.giftGiver == worker;
      }).giftReceiver = $scope.posibilities[(index + 1) % $scope.posibilities.length];
    });
  };

  $scope.clean = function (relation) {
    relation.giftReceiver = null;
  };

});

var shuffleArray = function (array) {
  var m = array.length, t, i;

  // While there remain elements to shuffle
  while (m) {
    // Pick a remaining element…
    i = Math.floor(Math.random() * m--);

    // And swap it with the current element.
    t = array[m];
    array[m] = array[i];
    array[i] = t;
  }

  return array;
};


