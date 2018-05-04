'use strict';

angular.module('secretPalApp')
  .controller('navCtrl', function($scope, Account, $location) {

  $scope.isAuthenticated = function() {
    return Account.isAuthenticated();
  };

  $scope.isAdmin = function(){
    return Account.isAdmin();
  };

  $scope.logout = function () {
    Account.logout();
    Account.isAuthenticated();
    return $location.path('/');
  };

});
