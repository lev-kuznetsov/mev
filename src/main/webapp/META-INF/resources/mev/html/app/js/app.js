'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers']).
  config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl: 'partials/home-view.html', 
		controller: 'HomeCtrl'});
	$routeProvider.when('/about', {
		templateUrl: 'partials/about-view.html', 
		controller: 'HeatmapCtrl'});
    $routeProvider.when('/heatmap/:matrixLocation', {
		templateUrl: 'partials/heatmap-view.html', 
		controller: 'HeatmapCtrl'});
    $routeProvider.otherwise({redirectTo: '/home'});
  }]);
