'use strict';

var app = angular.module('secretPalApp');
app.controller('GameStatusController', function ($scope, $modal, $rootScope, MailService, FriendRelationService) {

    FriendRelationService.all( function(data) {
        $scope.friendRelations = data;});


    MailService.all(function(data){ $scope.messageFailure = data;});


    $scope.today = new Date();
    $scope.hasGiverGiveGift = function (giftGiver) {
        return giftGiver.giftDateReceived !== null? "Si" : "No";
    };

    $scope.hasBirthdayMail = function(giftReceiver){
        var date = giftReceiver.dateOfBirth;
        var unDia = 24*60*60*1000; // hora*minuto*segundo*milli
        var birthday = new Date(date);
        birthday.setYear($scope.today.getFullYear());
        var diff = Math.round((birthday.getTime() - $scope.today.getTime())/unDia);
        var hasNotMailFailure = true;
        if ($scope.messageFailure.length !== 0) {
            $scope.messageFailure.forEach(function (message) {
                message.subject.includes(giftReceiver.fullName)?hasNotMailFailure = false:hasNotMailFailure;
            });
        }
        return ((diff < 0) && hasNotMailFailure)? "Si" : "No";
    };


    $scope.hasFailureMailRelation = function (giftGiver) {
        var hasNotFailureMail = true;
        if($scope.messageFailure.length !== 0){
            $scope.messageFailure.forEach(function (message) {
                 message.recipient === giftGiver.eMail? hasNotFailureMail = false:hasNotFailureMail;
            });
        }
        return hasNotFailureMail? "Si" : "No";
    };



});
