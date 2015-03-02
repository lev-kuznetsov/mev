define([], function(){
	"use strict";
	var AnnotationSetMeta = function(raw){
		
		//init
		var that={};
		var _data = {};	
		
		
		_data=raw;
		
		
		//public
		that.getName=function(){
			return _data.name;
		};
		that.getDescription=function(){
			return _data.description;
		};
		that.getNumberOfSamples=function(){
			return _data.numberOfSamples;
		};
		return that;
	};
	return AnnotationSetMeta;
});