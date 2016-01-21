define(["d3", "crossfilter"], function(d3, crossfilter){
	"use strict";

	var D3Crossfilter = function D3Crossfilter(data, scale){
		var _self = this;
		var xf = crossfilter(data);    	
    	var xDim = xf.dimension(function(d) { return d.x; });
    	var yDim = xf.dimension(function(d) { return d.y; });
		function 
	};

	D3Crossfilter.$inject = [];
	D3Crossfilter.$name = "D3Crossfilter";
	D3Crossfilter.$provider = "Service";
	return D3Crossfilter;
});