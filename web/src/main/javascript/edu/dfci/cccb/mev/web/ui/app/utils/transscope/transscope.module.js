define(["ng"], function(ng){
	var module = ng.module("mui.utils.transscope", []);
	module.directive('myDirective', function(setTransscope) {
	  return {
	    scope: {
	      display: "="
	    },
	    transclude: true,   // required
	    restrict: 'EA',
	    templateUrl: 'my-template.html',
	    link: setTransscope
	    
	    /* ... or call within your own link function:
	    link: function($scope, $element) {
	      setTransscope($scope, $element);
	      
	      // ...
	      // Other stuff
	      // ...
	    } */
	    
	  };
	});	
	module.factory('setTransscope', function() {
	  return function setTransscope($scope, $element) {
	    $element.data('transscope', $scope);
	    console.debug("set transscope: $element, $scope", $element, $scope);
	  };
	});
	module.directive('ngTransscope', function() {
	  return {
	    restrict: 'EAC',
	    link: function($scope, $element, $attrs, $ctrls, $transclude) {
	      console.debug("link ngTransscope");
	      if(!$transclude) {
	        throw Error('No parent directive that requires a transclusion found.');
	      }
	      
	      var targetScope = $element.inheritedData('transscope');
	      
	      if(!targetScope) {
	        throw Error('No transscope defined by parent directive.');
	      }
	      
	      $transclude(targetScope.$new(), function(clone) {
//	        $element.empty();
	    	console.debug("transscope transclude - clone", targetScope, clone);
	        $element.append(clone);
//	    	$element.children().append(clone);
	      });
	    }
	  };
	});
	return module;
});