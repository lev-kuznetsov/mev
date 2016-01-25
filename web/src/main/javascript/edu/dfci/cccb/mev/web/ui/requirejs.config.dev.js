
var REQUIREJS_CONFIG = (function(){
	var paths = {
		vendor: function(path){
			return "/container/vendor/"+path;
		},
		app: function(path){
			return "/container/ui/app/"+path;
		},
		bower: function(path){
			return this.vendor("bower_components/"+path);
		},
		js: function(path){
			return this.vendor("/container/javascript/"+path);
		}
	};
	
	return {
		wait: 20,
		baseUrl: "/container/javascript",
		paths: {
			jquery : ["//code.jquery.com/jquery-2.1.1"],
			bootstrap: [paths.vendor("bootstrap-3.3.1/dist/js/bootstrap")],
			ngX : [paths.bower("angular/angular"), "//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular", paths.vendor("angularjs/angular"), "//ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular"],
			ng: [paths.app("mui-ng")],
			mui: [paths.app("mui")],
			"angular-animate": [paths.bower("angular-animate/angular-animate"), "//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-animate.min"],
			"angular-ui-bootstrap": [paths.vendor("uibootstrap/ui-bootstrap-tpls-0.12.1.min")],			
			"angular-ui-router" :  [paths.vendor("uirouter/angular-ui-router")], //, "//cdnjs.cloudflare.com/ajax/libs/angular-ui-router/0.2.12/angular-ui-router"
			"angular-resource": [paths.vendor("angularjs/angular-resource")],
			appjs: [paths.app("app")],
			q: [paths.vendor("q/q")],
			underscore: [paths.vendor("underscore/underscore")],
			lodash: [paths.vendor("lodash/lodash")],
			angularTreeView: [paths.vendor("angular-treeview/treeView")],
			lessjs: [paths.vendor("lessjs/less.min.js")],
			bootstrapTree: [paths.vendor("bootstrap-tree/bootstrap-tree")],
			elementQueries: [paths.vendor("marcj/css-element-queries/ElementQueries")],
			resizeSensor: [paths.vendor("marcj/css-element-queries/ResizeSensor")],
			"angular-route" : [ paths.vendor('angularjs/angular-route') ],
			"jquery-ui" : ['//code.jquery.com/ui/1.9.2/jquery-ui.min', 'jquery-ui/1.9.2/jquery-ui.min'],
			d3 : [ '//cdnjs.cloudflare.com/ajax/libs/d3/3.5.9/d3' ],
			nvd3: [paths.vendor('nvd3/build/nv.d3'), '//cdnjs.cloudflare.com/ajax/libs/nvd3/1.8.1/nv.d3'],
			"angular-nvd3" : [ paths.vendor('angular-nvd3/dist/angular-nvd3') ],
		    retina : [ '/library/webjars/retinajs/0.0.2/retina' ],
		    notific8 : [ 'notific8.min' ],
		    "ng-grid" : [ '//cdnjs.cloudflare.com/ajax/libs/ng-grid/2.0.7/ng-grid', '/container/javascript/ng-grid-2.0.7.min',  ],
		    blob : [ '/container/javascript/canvasToBlob/Blob' ],
		    canvasToBlob : [ '/container/javascript/canvasToBlob/canvas-toBlob' ],
		    "browser-filesaver" : [ '/container/javascript/fileSaver/FileSaver' ],
		    qtip : [ '//cdnjs.cloudflare.com/ajax/libs/qtip2/2.1.1/jquery.qtip', '/library/webjars/qtip2/2.1.1/jquery.qtip' ],
		    log4js : [ '//cdnjs.cloudflare.com/ajax/libs/log4javascript/1.4.9/log4javascript', '/library/webjars/log4javascript/1.4.5/log4javascript' ],
		    "angular-utils-pagination": [paths.vendor('mbAngularUtils/pagination/dirPagination')],
			"angular-utils-ui-breadcrumbs": [paths.vendor('mbAngularUtils/breadcrumbs/uiBreadcrumbs')],
			"js-data" : ['//cdnjs.cloudflare.com/ajax/libs/js-data/2.3.0/js-data'],
			"js-data-angular" : ['//cdnjs.cloudflare.com/ajax/libs/js-data-angular/3.0.0/js-data-angular'],
			"d3-tip": ["//cdnjs.cloudflare.com/ajax/libs/d3-tip/0.6.7/d3-tip.min"],
			"pouchdb": ["//cdn.jsdelivr.net/pouchdb/5.1.0/pouchdb.min"],
			"blob-util": [paths.vendor("blob-util/dist/blob-util")],
			pouchDbLru: [paths.vendor("pouchdb-lru-cache/dist/pouchdb.lru-cache")],
			jsLru: [paths.vendor("rsms/js-lru/lru")],
			"ag-grid": [paths.bower("ag-grid/dist/ag-grid")],
			"crossfilter": [paths.bower("crossfilter/crossfilter")],
			"mev-scatter-plot": [paths.app("widgets/common/plots/scatterPlot/widgets.common.plots.scatterPlot.module")]
		},		
		map: {
	        '*': {
	            angular: 'ng',
	        }	        
	    },
		
		shim: {
			jquery: {
				exports: "$"
			},
			"jquery-ui": {
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
			"angular-animate": {
				deps: ["ng"]
			},
			log4js : {
			    exports : 'log4javascript'
			},
			"angular-ui-bootstrap": {
				deps: ["ng"]
			},
			"angular-ui-router": {
				deps: ["ng"]
			},
			q: {				
				exports: "q"
			},
			"angular-resource": {
				deps: ["ng"]
			},
			angularTreeView: {
				deps: ["ng"]
			},
			"pouchdb": {
				exports: "PouchDB"
			},
			pouchDbLru: {
				deps: ["PouchDB"]				
			},
			"blob-util": {
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
			"angular-route": {
				deps: ["ng", "jquery"]
			},
			notific8: {
		      deps : [ 'jquery' ],
		      exports : 'notific8'
		    },
		    "ng-grid": {
		    	deps: ['jquery', 'ng']
		    },
		    "ag-grid": {
		    	deps: ['jquery', 'ng']
		    },
		    'browser-filesaver' : {
		        deps : [ 'canvasToBlob' ],
		        exports : 'fileSaver'
		    },
		    "angular-utils-pagination": {
		    	deps: ['jquery', 'angular']
		    }, 
		    "angular-utils-ui-breadcrumbs": {
		    	deps: ['jquery', 'angular']
		    },
		    "d3-tip": {
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
		    "angular-nvd3": {
		    	deps: ["angular", "nvd3"]		    	
		    },
		    crossfilter: {
		    	deps: [],
		    	exports: "crossfilter"
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
//		{
//		    name : "clinical",
//		    location : "/container/javascript/clinicalSummary",
//		    main : "ClinicalSummary.package"
//		},
		{
			name: "js",
			location : "/container/javascript"
		}
	  ]
	}
})();