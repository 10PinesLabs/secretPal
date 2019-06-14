[![Build Status](https://travis-ci.org/10PinesLabs/secretPal.svg?branch=master)](https://travis-ci.org/10PinesLabs/secretPal)

# 10Pines-secretPal

##¿Qué se puede hacer con Secret Pal?

* Ver qué pino tenés asignado como amigo invisible 
* Ver la lista de deseos de los pinos 
* Agregar ítems a la lista de deseos de un pino 


# Configurando el ambiente de desarrollo

# Backend 

0. Descargar proyecto

## Crear la base de datos para levantar la aplicacion del backend
1. `sudo apt install postgresql`
2. `sudo -u postgres createuser -P pal`
3. `sudo -u postgres psql`. (entramos a la bd)
4. `create database secret_pal (secret_pal_test para los tests)`
5. `grant all privileges on database secret_pal to pal`(_test para tests) 

# Frontend

## Levantar el frontend con npm
1. `npm install`
2. `npm install -g grunt-cli`
3. `npm install -g bower`
4. `bower install`
5. `grunt serve`


# Backlog

https://trello.com/b/QYdfnd7H/secretpal


