define(['angular', 'directives', 'services', 'controllers'], function(angular){
	'use strict';
	return angular.module('myApp', [
	     'myApp.directives', 
	     'myApp.services',
	     'myApp.controllers'])
	.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
	  
	  $routeProvider
		  .when('/heatmap', {
			  templateUrl: '/container/view/partials/heatmap', 
			  controller: 'HeatmapCtrl'
		  })
		  .when('/home', {
        templateUrl: '/container/view/partials/importItems'
      });
		  
		  $routeProvider.otherwise({redirectTo: '/home'});
		  
		  //$locationProvider.html5Mode(true);
		  
		}]);
	
});