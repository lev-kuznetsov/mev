define(["ng"],function(ng){
	var $injector = ng.injector(['ng']);
	var $q = $injector.get('$q');
	return $q;
});