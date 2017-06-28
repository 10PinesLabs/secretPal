'use strict';

var app = angular.module('secretPalApp');
app.controller('FriendRelationController', function ($scope, $modal, $filter, FriendRelationService, SweetAlert) {

  FriendRelationService.allPosibleRelations( function(data) {
    $scope.posibleRelations = data;
  });

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

  $scope.auto = function(){
    SweetAlert.swal({
      title: "Actualizando",
      text: "Esto puede tardar un rato...\n muchos algoritmos",
      showConfirmButton: false
    });

    FriendRelationService.autoAssign( function(data) {
      $scope.friendRelations = data;
    });

  };

  $scope.clean = function (relation) {
    relation.giftReceiver = null;
  };

  $scope.update = function(giver, receiver) {
    FriendRelationService.update(giver, receiver, function(data) {
      FriendRelationService.allPosibleRelations( function(data) {
        $scope.posibleRelations = data;
      });
    });
  };

  $scope.delete = function (giver) {
    SweetAlert.swal({
        title: "Estas seguro?",
        text:  + " no será amigo invisible de nadie!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Si, borrar!",
        closeOnConfirm: false
      },
      function (isConfirm) {
        if (isConfirm) {
          FriendRelationService.delete(giver, function () {
            FriendRelationService.allPosibleRelations( function(data) {
              $scope.posibleRelations = data;
            });
            SweetAlert.swal("Se ha borrado exitosamente");
          });
        }
      });
  };

});
