define(['angular'], function(angular){

	return angular.module('myApp.directives', [])
	.directive('menubar', function() {
	
	    return {
	      restrict: 'E',
	      template: 'Working Fine'
	    };
    
	});

});