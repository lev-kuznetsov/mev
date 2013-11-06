define(['angular', 'jquery'], function(angular, $){

  return angular.module('myApp.controllers', []).
  controller('HeatmapCtrl', ['$scope', '$routeParams', function($scope, $routeParams) {
          
    $scope.heatmapId = "Heatmap";
    
    var h = $('#handle'),
    l = $('#left'),
    r = $('#right'),
    w = $('body').width() - 18;

	var isDragging = false;
	
	h.mousedown(function(e){
	    isDragging = true;
	    e.preventDefault();
	});
	
	$scope.expandLeft = function(){
			$('#rightPanel').attr("class", "span3");
			$('#leftPanel').attr("class", "span9");
		
	};
	
	
	
	$scope.expandRight = function(){

			$('#leftPanel').attr("class", "span3");
			$('#rightPanel').attr("class", "span9");

		
	};
	
	$scope.expandBoth = function() {
		
		
	};
	
	$(document).mouseup(function(){
	    isDragging = false;
	}).mousemove(function(e){
	    if(isDragging && e.pageX < w*3/4 && e.pageX > w/4){
	        l.css('width', e.pageX);
	    r.css('width', w - e.pageX);
	    }
	});
    
  }]);
  
});