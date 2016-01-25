/* Use requirejs to load other modules located under this package
 * 
 * Refer to edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations
 * for url locations, currently this folder is mapped at /container/javascript
 */

require.config ({  
  baseUrl : "/container/javascript",
  paths : {
    jquery : [ '//codeorigin.jquery.com/jquery-2.1.0', '/library/webjars/jquery/2.1.0/jquery' ],
    jqueryUi : ['//code.jquery.com/ui/1.9.2/jquery-ui.min', 'jquery-ui/1.9.2/jquery-ui.min'],
    angular : [ '//ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular',
               '/library/webjars/angularjs/1.2.13/angular' ],   
    "angular-route" : [ 'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-route.min',
                    '/library/webjars/angularjs/1.2.13/angular-route.min' ],
    angularResource : [ 'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-resource.min',
                       '/library/webjars/angularjs/1.2.13/angular-resource.min' ],
    bootstrap : [ '//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min',
                 '/library/webjars/bootstrap/3.1.1/js/bootstrap.min' ],
    // uiBootstrap:
    // ['//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.10.0/ui-bootstrap.min',
    // 'bootstrap-ui'],
    // uiBootstrapTpls:
    // ['//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.10.0/ui-bootstrap-tpls.min',
    // 'bootstrap-ui-tpls'],
    uiBootstrapTpls : [ '/container/javascript/ui-bootstrap-tpls-0.11.0-SNAPSHOT', 'bootstrap-ui-tpls' ],
    d3 : [ '//cdnjs.cloudflare.com/ajax/libs/d3/3.4.1/d3', '/library/webjars/d3js/3.4.1/d3.min' ],
    retina : [ '/library/webjars/retinajs/0.0.2/retina' ],
    notific8 : [ 'notific8.min' ],
    ngGrid : [ '//cdnjs.cloudflare.com/ajax/libs/ng-grid/2.0.7/ng-grid', '/container/javascript/ng-grid-2.0.7.min',  ],
    blob : [ '/container/javascript/canvasToBlob/Blob' ],
    canvasToBlob : [ '/container/javascript/canvasToBlob/canvas-toBlob' ],
    fileSaver : [ '/container/javascript/fileSaver/FileSaver' ],
    qtip : [ '/library/webjars/qtip2/2.1.1/jquery.qtip' ],
    'log4js' : [ '/library/webjars/log4javascript/1.4.5/log4javascript' ],
    lodash: ["//cdnjs.cloudflare.com/ajax/libs/lodash.js/2.4.1/lodash.min"],
    "angular-ui-router" :  ["//cdnjs.cloudflare.com/ajax/libs/angular-ui-router/0.2.13/angular-ui-router.min"],
    mbAngularUtilsBreadcrumbs: ['/container/vendor/mbAngularUtils/breadcrumbs/uiBreadcrumbs']
  },
  shim : {
    'angular' : {
      exports : 'angular',
      deps: ["jquery"]
    },
    'angular-route' : {
      exports : 'ngRoute',
      deps : [ 'angular' ]
    },
    'angularResource' : {
      exports : 'ngResource',
      deps : [ 'angular' ]
    },
    'uiBootstrapTpls' : {
      deps : [ 'angular', 'bootstrap' ]
    },
    'd3' : {
      exports : 'd3'
    },
    'bootstrap' : {
      deps : [ 'jquery' ]
    },
    'css-loader' : {
      deps : [ 'bootstrap' ]
    },
    'notific8' : {
      deps : [ 'jquery' ],
      exports : 'notific8'
    },
    'ngGrid' : {
      deps : [ 'jquery', 'angular', 'uiBootstrapTpls' ]
    },
    'canvasToBlob' : {
      deps : [ 'blob' ],
      exports : 'canvasToBlob'
    },
    'fileSaver' : {
      deps : [ 'canvasToBlob' ],
      exports : 'fileSaver'
    },
    'jqueryUi': {
        deps : ['jquery', 'css-loader']
    },
    'qtip': {
      deps : ['jquery', 'imagesloaded'],
      exports : 'qtip'
    },
    'log4js' : {
      exports : 'log4javascript'
    },
    "angular-ui-router": {
		deps: ["angular"]
	},
	mbAngularUtilsBreadcrumbs: {
    	deps: ['jquery', 'angular']
    }
  },
  packages : [ {
	    name : "mainmenu",
	    location : "/container/javascript/mainmenu",
	    main : "MainMenu.package"
  	}, {
	    name : "geods",
	    location : "/container/javascript/geods",
	    main : "Geods.package"
	}, {
	    name : "clinical",
	    location : "/container/javascript/clinicalSummary",
	    main : "ClinicalSummary.package"
	} ],
  waitSeconds : "600"

});

require ([ 'jquery',
          'angular',
          'app',
          'orefine/OrefineBridge',
          'blob',
          'canvasToBlob',
          'fileSaver',
          'bootstrap',
          'css-loader',
          'setmanager/SetManager',
          'presets/PresetManager',
          'mainpanel/MainPanel',
          'retina',
          'ngGrid',
          'mainmenu',
          'geods',
          'clinical',
          'log'
          ], function (jquery, angular, app, orb) {
  
  'use strict';
  var $html = angular.element (document.getElementsByTagName ('html')[0]);
  angular.element ().ready (function () {
//    $html.addClass ('ng-app');
//	try{
		angular.bootstrap ($html, [ app['name'] ]);
//	}
//    catch (e) {
//        console.error(e.stack || e.message || e);
//    }

  });

  window.OpenRefineBridge = orb;
});
