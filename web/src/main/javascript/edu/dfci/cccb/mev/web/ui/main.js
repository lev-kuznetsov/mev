(function(){
	
	var paths = {
		vendor: function(path){
			return "/container/vendor/"+path;
		},
		app: function(path){
			return "/container/ui/app/"+path;
		}
	};
	
	requirejs.config({
		baseUrl: "/container/javascript",
		paths: {
			jquery : ["//code.jquery.com/jquery-2.1.1.min", paths.vendor("jquery/jquery-2.1.1")],
			bootstrap: ['//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min'],
			ng : ["//ajax.googleapis.com/ajax/libs/angularjs/1.2.27/angular.min"],			
			uibootstrap: ["//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.12.1/ui-bootstrap-tpls.min"],			
			"angular-ui-router" :  ["//cdnjs.cloudflare.com/ajax/libs/angular-ui-router/0.2.13/angular-ui-router.min"],
			ngresource: ["//cdnjs.cloudflare.com/ajax/libs/angular.js/1.2.27/angular-resource.min"],
			appjs: [paths.app("app")],
			lodash: ["//cdnjs.cloudflare.com/ajax/libs/lodash.js/2.4.1/lodash.min"],			
			angularRoute : [ "//cdnjs.cloudflare.com/ajax/libs/angular.js/1.2.27/angular-route.min" ],
			"jquery-ui" : ['//code.jquery.com/ui/1.9.2/jquery-ui.min', 'jquery-ui/1.9.2/jquery-ui.min'],
			d3 : [ '//cdnjs.cloudflare.com/ajax/libs/d3/3.4.1/d3', '/library/webjars/d3js/3.4.1/d3.min' ],
		    retina : [ '/library/webjars/retinajs/0.0.2/retina' ],
		    notific8 : [ 'notific8.min' ],
		    "ng-grid" : [ '//cdnjs.cloudflare.com/ajax/libs/ng-grid/2.0.7/ng-grid', '/container/javascript/ng-grid-2.0.7.min',  ],
		    blob : [ '/container/javascript/canvasToBlob/Blob' ],
		    canvasToBlob : [ '/container/javascript/canvasToBlob/canvas-toBlob' ],
		    fileSaver : [ '/container/javascript/fileSaver/FileSaver' ],
		    qtip : [ '/library/webjars/qtip2/2.1.1/jquery.qtip' ],
		    log4js : [ '/library/webjars/log4javascript/1.4.5/log4javascript'],
		    "angular-utils-pagination": [paths.vendor('mbAngularUtils/pagination/dirPagination')],
			"angular-utils-ui-breadcrumbs": [paths.vendor('mbAngularUtils/breadcrumbs/uiBreadcrumbs')],
//			jsData : ['//cdnjs.cloudflare.com/ajax/libs/js-data/2.0.0/js-data.min'],
			jsDataAngular : ['//cdnjs.cloudflare.com/ajax/libs/js-data/2.0.0/js-data-angular.min'],
		},
		
		map: {
	        '*': {
	            angular: 'ng',
	            angularResource: 'ngresource'
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
			ng: {
				deps: ["jquery"],
				exports: "angular"
			},
			ngmocks: {
				deps: ["ng"]
			},
			uibootstrap: {
				deps: ["ng"]
			},
			"angular-ui-router": {
				deps: ["ng"]
			},
			ngresource: {
				deps: ["ng"]
			},
			angularTreeView: {
				deps: ["ng"]
			},
			angularData: {
				deps: ["ng"]
			},
			angularRoute: {
				deps: ["ng", "jquery"]
			},
			notific8: {
		      deps : [ 'jquery' ],
		      exports : 'notific8'
		    },
		    "ng-grid": {
		    	deps: ['jquery', 'ng']
		    },
		    'browser-filesaver' : {
		        deps : [ 'canvasToBlob' ],
		        exports : 'fileSaver'
		    },
		    "angular-utils-pagination": {
		    	deps: ['jquery', 'angular']
		    }, 
		    mbAngularUtilsBreadcrumbs: {
		    	deps: ['jquery', 'angular']
		    },
		    jsDataAngular: {
		    	deps: ['jsData', 'ng']
		    }
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
	});
	
//	requirejs.onError = function (err) {
//	    console.log(err.requireType);
//	    if (err.requireType === 'timeout') {
//	        console.log('modules: ' + err.requireModules);
//	    }
//
//	    throw err;
//	};
	
	// Usage:
	// Put this in a separate file and load it as the first module
	// (See https://github.com/jrburke/requirejs/wiki/Internal-API:-onResourceLoad)
	// Methods available after page load:
	// rtree.map()
	// - Fills out every module's map property under rtree.tree.
	// - Print out rtree.tree in the console to see their map property.
	// rtree.toUml()
	// - Prints out a UML string that can be used to generate UML
	// - UML Website: http://yuml.me/diagram/scruffy/class/draw
	requirejs.onResourceLoad = function (context, map, depMaps) {
	    if (!window.rtree) {
	        window.rtree = {
	            tree: {},
	            map: function() {
	                for (var key in this.tree) {
	                    if (this.tree.hasOwnProperty(key)) {
	                        var val = this.tree[key];
	                        for (var i =0; i < val.deps.length; ++i) {
	                            var dep = val.deps[i];
	                            val.map[dep] = this.tree[dep];
	                        }
	                    }
	                }
	            },
	            toUml: function() {
	                var uml = [];
	 
	                for (var key in this.tree) {
	                    if (this.tree.hasOwnProperty(key)) {
	                        var val = this.tree[key];
	                        for (var i = 0; i < val.deps.length; ++i) {
	                            uml.push("[" + key + "]->[" + val.deps[i] + "]");
	                        }
	                    }
	                }
	 
	                return uml.join("\n");
	            }
	        };
	    }
	 
	    var tree = window.rtree.tree;
	 
	    function Node() {
	        this.deps = [];
	        this.map = {};
	    }
	 
	    if (!tree[map.name]) {
	        tree[map.name] = new Node();
	    }
	 
	    // For a full dependency tree
	    if (depMaps) {
	        for (var i = 0; i < depMaps.length; ++i) {
	            tree[map.name].deps.push(depMaps[i].name);
	        }
	    }
	 
	// For a simple dependency tree
	 
//	    if (map.parentMap && map.parentMap.name) {
//	        if (!tree[map.parentMap.name]) {
//	            tree[map.parentMap.name] = new Node();
//	        }
	//
//	        if (map.parentMap.name !== map.name) {
//	            tree[map.parentMap.name].deps.push(map.name);
//	        }
//	    }
	    
	};
	
	requirejs(["ng", "appjs", 'orefine/OrefineBridge'], function(ng, app, OpenRefineBridge){
		ng.element(document).ready(function(){
			ng.bootstrap(document, [app.name]);
		});
		window.OpenRefineBridge = OpenRefineBridge;
	});
	
	
}());