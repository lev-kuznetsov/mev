/* Use requirejs to load other modules located under this package
 * 
 * Refer to edu.dfci.cccb.mev.web.configuration.container.ContainerConfigurations
 * for url locations, currently this folder is mapped at /container/javascript
 */

require.config ({
  baseUrl : "/container/javascript",
  paths : {
    jquery : [ 'http://codeorigin.jquery.com/jquery-2.1.0', 
               '/library/webjars/jquery/2.1.0/jquery' ],
    angular : [ 'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular',
               '/library/webjars/angularjs/1.2.13/angular' ],
    angularRoute : [ 'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-route.min',
                           '/library/webjars/angularjs/1.2.13/angular-route.min' ],
    angularResource : [ 'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-resource',
                           '/library/webjars/angularjs/1.2.13/angular-resource' ],
    bootstrap : [ '//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min',
                 '/library/webjars/bootstrap/3.1.1/js/bootstrap.min' ],
    bootstrapUi: ['//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.4.0/ui-bootstrap-tpls.min',
                   'bootstrap-ui'],
    d3 : [ '//cdnjs.cloudflare.com/ajax/libs/d3/3.4.1/d3', 
           '/library/webjars/d3js/3.4.1/d3.min' ],
    retina : [ '/library/webjars/retinajs/0.0.2/retina' ],
    notific8 : [ 'notific8.min' ]
  },
  shim : {
    'angular' : {
      exports : 'angular'
      
    },
    'angularRoute' : {
        exports : 'ngRoute',
        deps: ['angular']
    },
    'angularResource' : {
        exports : 'ngResource',
        deps: ['angular']
    },
    'bootstrapUi':{
            exports : 'ui.bootstrap',
            deps:['angular']
    },
    'd3' : {
      exports : 'd3'
    },
    'bootstrap' : {
      deps : [ 'jquery' ]
    },
    'bootstrap-css' : {
      deps : [ 'bootstrap' ]
    },
    'notific8' : {
      deps : [ 'jquery' ],
      exports : 'notific8'
    }

  },
  waitSeconds : "2"

});

require ([ 'jquery',
          'angular',
          'app',
          'orefine/OrefineBridge',
          'bootstrap-css',
          'bootstrap',
          'setmanager/SetManager',
          'presets/PresetManager',
          'mainpanel/MainPanel',
          'retina' ], function (jquery, angular, app, orb) {

  'use strict';
  var $html = angular.element (document.getElementsByTagName ('html')[0]);
  angular.element ().ready (function () {
    $html.addClass ('ng-app');
    angular.bootstrap ($html, [ app['name'] ]);

  });

  window.OpenRefineBridge = orb;
});
