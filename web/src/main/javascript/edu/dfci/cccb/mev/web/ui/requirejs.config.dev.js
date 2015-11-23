
var REQUIREJS_CONFIG = (function(){
	var paths = {
		vendor: function(path){
			return "/container/vendor/"+path;
		},
		app: function(path){
			return "/container/ui/app/"+path;
		}
	};
	
	return {
		baseUrl: "/container/javascript",
		paths: {
			jquery : ["//code.jquery.com/jquery-2.1.1", paths.vendor("jquery/jquery-2.1.1")],
			bootstrap: [paths.vendor("bootstrap-3.3.1/dist/js/bootstrap")],
			ngX : ["//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular", paths.vendor("angularjs/angular"), "//ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular"],
			ng: [paths.app("mui-ng")],
			mui: [paths.app("mui")],
			ngAnimate: ["//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-animate.min"],
			uibootstrap: [paths.vendor("uibootstrap/ui-bootstrap-tpls-0.12.1.min")],			
			nguirouter :  [paths.vendor("uirouter/angular-ui-router"), "//cdnjs.cloudflare.com/ajax/libs/angular-ui-router/0.2.12/angular-ui-router"],
			ngresource: [paths.vendor("angularjs/angular-resource")],
			appjs: [paths.app("app")],
			q: [paths.vendor("q/q")],
			underscore: [paths.vendor("underscore/underscore")],
			lodash: [paths.vendor("lodash/lodash")],
			angularTreeView: [paths.vendor("angular-treeview/treeView")],
			lessjs: [paths.vendor("lessjs/less.min.js")],
			bootstrapTree: [paths.vendor("bootstrap-tree/bootstrap-tree")],
			elementQueries: [paths.vendor("marcj/css-element-queries/ElementQueries")],
			resizeSensor: [paths.vendor("marcj/css-element-queries/ResizeSensor")],
			angularRoute : [ paths.vendor('angularjs/angular-route') ],
			jqueryUi : ['//code.jquery.com/ui/1.9.2/jquery-ui.min', 'jquery-ui/1.9.2/jquery-ui.min'],
			d3 : [ '//cdnjs.cloudflare.com/ajax/libs/d3/3.5.9/d3' ],
			nvd3: ['//cdnjs.cloudflare.com/ajax/libs/nvd3/1.8.1/nv.d3'],
			angularNvd3 : [ paths.vendor('angular-nvd3/dist/angular-nvd3') ],
		    retina : [ '/library/webjars/retinajs/0.0.2/retina' ],
		    notific8 : [ 'notific8.min' ],
		    ngGrid : [ '//cdnjs.cloudflare.com/ajax/libs/ng-grid/2.0.7/ng-grid', '/container/javascript/ng-grid-2.0.7.min',  ],
		    blob : [ '/container/javascript/canvasToBlob/Blob' ],
		    canvasToBlob : [ '/container/javascript/canvasToBlob/canvas-toBlob' ],
		    fileSaver : [ '/container/javascript/fileSaver/FileSaver' ],
		    qtip : [ '//cdnjs.cloudflare.com/ajax/libs/qtip2/2.1.1/jquery.qtip', '/library/webjars/qtip2/2.1.1/jquery.qtip' ],
		    log4js : [ '//cdnjs.cloudflare.com/ajax/libs/log4javascript/1.4.9/log4javascript', '/library/webjars/log4javascript/1.4.5/log4javascript' ],
		    mbAngularUtilsPagination: [paths.vendor('mbAngularUtils/pagination/dirPagination')],
			mbAngularUtilsBreadcrumbs: [paths.vendor('mbAngularUtils/breadcrumbs/uiBreadcrumbs')],
			jsData : ['//cdnjs.cloudflare.com/ajax/libs/js-data/2.3.0/js-data'],
			jsDataAngular : ['//cdnjs.cloudflare.com/ajax/libs/js-data-angular/3.0.0/js-data-angular'],
			d3tip: ["//cdnjs.cloudflare.com/ajax/libs/d3-tip/0.6.7/d3-tip.min"],
			PouchDB: ["//cdn.jsdelivr.net/pouchdb/5.1.0/pouchdb.min"],
			blobUtil: [paths.vendor("blob-util/dist/blob-util")],
			pouchDbLru: [paths.vendor("pouchdb-lru-cache/dist/pouchdb.lru-cache")],
			jsLru: [paths.vendor("rsms/js-lru/lru")],
			
		},		
		map: {
	        '*': {
	            angular: 'ng',
	            angularResource: 'ngresource',
	            'js-data': 'jsData'	            
	        }	        
	    },
		
		shim: {
			jquery: {
				exports: "$"
			},
			jqueryUi: {
				deps: ["jquery"]
			},
			bootstrap: {
				deps: ["jquery"]
			},
			ngX: {
				deps: ["jquery"],
				exports: "angular"
			},
			ngmocks: {
				deps: ["ng"]
			},
			ngAnimate: {
				deps: ["ng"]
			},
			log4js : {
			    exports : 'log4javascript'
			},
			uibootstrap: {
				deps: ["ng"]
			},
			nguirouter: {
				deps: ["ng"]
			},
			q: {				
				exports: "q"
			},
			ngresource: {
				deps: ["ng"]
			},
			angularTreeView: {
				deps: ["ng"]
			},
			PouchDB: {
				exports: "PouchDB"
			},
			pouchDbLru: {
				deps: ["PouchDB"]				
			},
			blobUtil: {
				exports: "blobUtil"
			},
			jsLru: {
				exports: "LRUCache"
			},
//			angularData: {
//				deps: ["ng"]
//			},
			bootstrapTree: {
				deps: ["ng", "jquery"]
			},
			angularRoute: {
				deps: ["ng", "jquery"]
			},
			notific8: {
		      deps : [ 'jquery' ],
		      exports : 'notific8'
		    },
		    ngGrid: {
		    	deps: ['jquery', 'ng']
		    },
		    'fileSaver' : {
		        deps : [ 'canvasToBlob' ],
		        exports : 'fileSaver'
		    },
		    mbAngularUtilsPagination: {
		    	deps: ['jquery', 'angular']
		    }, 
		    mbAngularUtilsBreadcrumbs: {
		    	deps: ['jquery', 'angular']
		    },
		    d3tip: {
		    	deps: ['d3']
		    },
		    elementQueries: {
		    	exports: "ElementQueries"
		    },
		    resizeSensor: {
		    	deps: ["elementQueries"],
		    	exports: "ResizeSensor"
		    },		
		    nvd3: {
		    	deps: ["d3"],
		    	exports: "nv"
		    },
		    angularNvd3: {
		    	deps: ["angular", "nvd3"]		    	
		    }
//		    jsDataAngular: {
//		    	deps: ['ng', 'jsData']
//		    }
		},
	  packages : [ {
		    name : "mainmenu",
		    location : "/container/javascript/mainmenu",
		    main : "MainMenu.package"
	  	},
	  	{
	  		name: "app",
	  		location: "/container/ui/app"
	  	},
	  	{
		    name : "geods",
		    location : "/container/javascript/geods",
		    main : "Geods.package"
		},
		{
		    name : "clinical",
		    location : "/container/javascript/clinicalSummary",
		    main : "ClinicalSummary.package"
		}
	  ]
	}
})();