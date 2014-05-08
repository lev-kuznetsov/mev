define([], function(){
	
	var descriptor  = Object.getOwnPropertyDescriptor
	  , properties  = Object.getOwnPropertyNames
	  , define_prop = Object.defineProperty;
	
	
	
	return function extend(target, source) {
	    properties(source).forEach(function(key) {
	        define_prop(target, key, descriptor(source, key)) })

	    return target
	};

});
