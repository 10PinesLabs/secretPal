'use strict';

angular.module('secretPalApp')
    .controller('MainController', function($scope, Account, Token, $location) {
      Account.getCurrentAdmin().then(function(admin){
        $scope.admin = admin.data;
      });

      $scope.isAuthenticated = function() {
        return (Token.isAuthenticated());
      };

});
