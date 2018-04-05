'use strict';

angular
  .module('secretPalApp', [
    'ui.bootstrap',
    'ngAnimate',
    'ngMessages',
    'ngRoute',
    'satellizer',
    'oitozero.ngSweetAlert',
    'toggle-switch',
    'ngPageTitle'
  ])
  .config(function ($routeProvider) {
    var authenticated = function (Account, $location) {
      if (!Account.isAuthenticated()) {
        $location.path('/login');
      }
      return Account.getProfile();
    };

    var authenticatedAndAdmin = function (Account, $location) {
      if (!Account.isAdmin() || !Account.isAuthenticated()) {
        $location.path('/');
      }
      return Account.getProfile();
    };

    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainController',
        data: {
          pageTitle: 'Con cariño'
        }
      })
      .when('/mail', {
        templateUrl: 'views/mail.html',
        controller: 'MailController',
        resolve: { user : authenticatedAndAdmin },
        data: {
          pageTitle: 'Mails'
        }
      })
      .when('/workers', {
        templateUrl: '../views/workers.html',
        controller: 'WorkersController',
        resolve: { user : authenticatedAndAdmin },
        data: {
          pageTitle: 'Workers'
        }
      })
      .when('/friendRelations', {
        templateUrl: '../views/friendRelations.html',
        controller: 'FriendRelationController',
        resolve: { user : authenticatedAndAdmin },
        data: {
          pageTitle: 'Relaciones de amiguitos'
        }
      })
      .when('/wishlist', {
        templateUrl: '../views/wishlist.html',
        controller: 'WorkersController',
        resolve: { user : authenticated },
        data: {
          pageTitle: 'Pistas'
        }
      })
      .when('/hints', {
        templateUrl: '../views/hints.html',
        controller: 'WishlistController',
        resolve: { user : authenticated },
        data: {
          pageTitle: 'Wishlist'
        }
      })
      .when('/login', {
        templateUrl: '../views/login.html',
        controller: 'LoginController',
        data: {
          pageTitle: 'Login'
        }
      })
      .when('/register', {
        templateUrl: '../views/register.html',
        controller: 'RegisterController',
        data: {
          pageTitle: 'Registrar'
        }
      })
      .when('/profile', {
        templateUrl: '../views/profile.html',
        controller: 'ProfileController',
        resolve: { user : authenticated },
        data: {
          pageTitle: 'Perfil'
        }
      })
      .when('/confirmationGift', {
        templateUrl: '../views//confirmationGift.html',
        controller: 'ConfirmationGiftController',
        resolve: { user : authenticatedAndAdmin }
      })
      .when('/mailsFailure', {
        templateUrl: '../views/mailsFailure.html',
        controller: 'MailsFailureController',
        resolve: { user : authenticatedAndAdmin }
      })
      .when('/giftDefault', {
        templateUrl: '../views/giftDefault.html',
        controller: 'GiftDefaultController',
        resolve: { user : authenticatedAndAdmin }
      })
      .when('/ruleConfiguration', {
        templateUrl: '../views/ruleConfiguration.html',
        controller: 'CustomParticipantRuleController',
        resolve: { user : authenticatedAndAdmin }
      })
      .when('/gameStatus', {
        templateUrl: '../views/gameStatus.html',
        controller: 'GameStatusController',
        resolve: { user : authenticatedAndAdmin }
      });
  });
