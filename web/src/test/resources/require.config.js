  baseUrl : "",
  paths : {
    jquery : [ 'http://codeorigin.jquery.com/jquery-2.1.0', '/webjars/jquery/2.1.0/jquery' ],
    jqueryUi : ['https://code.jquery.com/ui/1.9.2/jquery-ui'],
    jasmineJquery : ['jasmine-jquery'],
    angular : [ 'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular',
               '/webjars/angularjs/1.2.13/angular' ],
    angularRoute : [ 'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-route',
                    '/webjars/angularjs/1.2.13/angular-route' ],
    angularResource : [ 'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-resource',
                       '/webjars/angularjs/1.2.13/angular-resource' ],
    angularAnimate : [ 'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-animate',
                       '/webjars/angularjs/1.2.13/angular-animate' ],
    angularMocks : [ 'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-mocks',
                      '/webjars/angularjs/1.2.13/angular-mocks' ],
    bootstrap : [ '//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap',
                 '/webjars/bootstrap/3.1.1/js/bootstrap' ],
    uiBootstrapTpls : [ 'ui-bootstrap-tpls-0.11.0-SNAPSHOT', 'bootstrap-ui-tpls' ],
    d3 : [ '//cdnjs.cloudflare.com/ajax/libs/d3/3.4.1/d3', '/webjars/d3js/3.4.1/d3.min' ],
    retina : [ '/webjars/retinajs/0.0.2/retina' ],
    notific8 : [ 'notific8.min' ],
    ngGrid : [ '//cdnjs.cloudflare.com/ajax/libs/ng-grid/2.0.7/ng-grid', 'ng-grid-2.0.7.min',  ],
    blob : [ 'canvasToBlob/Blob' ],
    canvasToBlob : [ 'canvasToBlob/canvas-toBlob' ],
    fileSaver : [ 'fileSaver/FileSaver' ],
    qtip : [ '/webjars/qtip2/2.1.1/jquery.qtip' ],
    'css-loader' : ['css-test'] ,
    'console':['console']
      //Added an empty file to point to since there is no head and css
      //can't actually load from css/styles container
  },
  shim : {
    'angular' : {
      exports : 'angular'

    },
    'angularRoute' : {
      exports : 'ngRoute',
      deps : [ 'angular' ]
    },
    'angularResource' : {
      exports : 'ngResource',
      deps : [ 'angular' ]
    },
    'angularMocks' : {
        exports : 'ngMocks',
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
    'jasmineJquery' : {
        deps : ['jquery']
    },
    'qtip': {
      deps : ['jquery', 'imagesloaded'],
      exports : 'qtip'
    }

  },
  packages : [ {
    name : "mainmenu",
    location : "mainmenu",
    main : "MainMenu.package"
  } ],
  waitSeconds : "2"