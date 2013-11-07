/* Use requirejs to load other modules located under this package
 * container.ContainerConfigurations
 * for url locations, currently this folder is mapped at /container/javascript
 */

require.config ({
	baseUrl: "/container/javascript",
	paths: {
		jquery: ['http://codeorigin.jquery.com/jquery-2.0.3',
		         '/library/webjars/jquery/2.0.3/jquery'],
		angular: ['https://ajax.googleapis.com/ajax/libs/angularjs/1.1.4/angular.min',
		          '/library/webjars/angularjs/1.1.4/angular']
	},
	shim: {
		 'angular': {
			 exports: 'angular'
		 }
	},
	waitSeconds: "2"
	

});

require( ['angular', 'app'], function(angular, app) {
		
	'use strict';
	var $html = angular.element(document.getElementsByTagName('html')[0]);
	angular.element().ready(function() {
		$html.addClass('ng-app');
		angular.bootstrap($html, [app['name']]);
	});
          
});





