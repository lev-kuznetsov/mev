define(["ng", "./AnnotationSetRepository"], 
function(ng, AnnotationSetRepository){
	var module = ng.module("mui.annotations", []);	
	module.service("AnnotationSetRepository", ["$q", "$timeout", "$http", AnnotationSetRepository]);
	return module;
});