'use strict';

angular.module('secretPalApp')
  .service('Account', function ($http, $rootScope, SweetAlert, $location, Token) {
    function buildRoute(path) {
      var route = '/api/auth';
      return route + path;
    }

    var self = this;

    self.getProfile = function () {
      return $http.get(buildRoute('/me'), {
        headers: {
          Authorization: Token.getToken()
        }
      }).then(function (response) {
        $rootScope.loggedUser = response.data;
        return response.data;
      });
    };

    self.getCurrentProfile = function () {
      return $rootScope.loggedUser;
    };

    self.isAuthenticated = function () {
      return Token.isAuthenticated()
    };

    self.logout = function () {
      return Token.logout()
    };

    self.getCurrentAdmin = function () {
      return $http.get(buildRoute('/admin'))
    };

    self.isAdmin = function () {
      if ($rootScope.loggedUser !== undefined) {
        return $rootScope.loggedUser.admin
      }
    };

    self.setCurrentAdmin = function (admin) {
      return $http.post(buildRoute('/admin'), admin).then(function () {
        SweetAlert.swal("Cambio de Admin", "El nuevo Admin es: " + admin.fullName, "success");
      }, function (data) {
        SweetAlert.swal("Algo salio mal", data, "error");
      });
    };

    self.login = function (credentials) {
      return $http.post(buildRoute('/login'), credentials)
        .then(function (response) {
          Token.saveToken(response.data.token);
          return self.getProfile()
        }).then(function (currentProfile) {
          SweetAlert.swal(SweetAlert.swal({
            title: "¡Bienvenido " + currentProfile.userName + "!",
            text: "Ingresaste correctamente",
            confirmButtonColor: "#68bd46",
          }));
          $location.path('/profile');
          return currentProfile;
        }).catch(function () {
          SweetAlert.swal("Usuario o contraseña invalida", "Por favor complete el formulario de registro o contactese con el Administrador", "error");
          $location.path('/login');
        })
    };

    self.register = function (newUser) {
      return $http.post(buildRoute('/register'), newUser).then(function () {
          SweetAlert.swal(SweetAlert.swal({
            title: "¡Registrado correctamente!",
            text: "Gracias por participar en ''Amigo invisible''",
            confirmButtonColor: "#68bd46",
          }));
          $location.path('/login');
      }).catch(function (error) {
        SweetAlert.swal("No te has registrado", error.data.message, "error");
        $location.path('/register');
      })
    };
  });
