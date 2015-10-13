define(["ng", "./plot/rhistPlotDirective"], 
function(ng){	
	return ng.module("mui.widgets.analysis.Histogram", arguments, arguments)
	.directive('onSizeChanged', ['$window', function ($window) {
	    return {
	        restrict: 'A',
	        scope: {
	            onSizeChanged: '&'
	        },
	        link: function (scope, $element, attr) {
	            var element = $element[0];

	            cacheElementSize(scope, element);
	            $window.addEventListener('resize', onWindowResize);

	            function cacheElementSize(scope, element) {
	                scope.cachedElementWidth = element.offsetWidth;
	                scope.cachedElementHeight = element.offsetHeight;
	            }

	            function onWindowResize() {
	                var isSizeChanged = scope.cachedElementWidth != element.offsetWidth || scope.cachedElementHeight != element.offsetHeight;
	                if (isSizeChanged) {
	                    var expression = scope.onSizeChanged();
	                    expression();
	                }
	            };
	        }
	    };
	}]);
});