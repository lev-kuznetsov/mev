define([], function(){
	"use strict";
	var Dashboard = function(raw){
		var self=this;
		
		//unwrap
		var _data=raw;					
		
		//public
		self.getFacets=function(){
			return _data.facets;
		};		
		self.getName=function(){
			return _data.annotationSet.meta.name;
		};
		self.getFacetCount=function(){
			return _data.facets.length;
		};
		self.toJson=function(){
			return _data;
		};
	};
	return Dashboard;
});