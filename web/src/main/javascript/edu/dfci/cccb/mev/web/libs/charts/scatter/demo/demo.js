define(["angular", "mev-scatter-plot"], function(ng){
	"use strict";
	return ng.module("demo", arguments, arguments)
	.controller("myCtrl", ["$scope", function($scope){
		var _self = this;
		$scope.data = "random";
		$scope.vm={
			logScaleX: false,
			logScaleY: false,
			zoomEnabled: false
		};
	}]);

    
});
