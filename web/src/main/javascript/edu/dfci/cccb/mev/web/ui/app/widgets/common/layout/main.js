(function(){
	
	requirejs.config({
		basePath: "/ngboostrap",
		paths: {
			jquery : ["//code.jquery.com/jquery-2.1.1", "vendor/jquery/jquery-2.1.1"],
			bootstrap: ["../../../../vendor/bootstrap-3.3.1/dist/js/bootstrap"],
			ng : ["../../../../vendor/angularjs/angular", "//ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular"],			
			uibootstrap: ["../../../../vendor/uibootstrap/ui-bootstrap-0.11.2.custom"],
			nguirouter :  ["../../../../vendor/uirouter/angular-ui-router", "//cdnjs.cloudflare.com/ajax/libs/angular-ui-router/0.2.12/angular-ui-router"],
			ngresource: ["../../../../vendor/angularjs/angular-resource"],
			appjs: ["widgets.common.layout.module"],
			q: ["../../../../vendor/q/q.hack"],
			ngmocks: ["../../../../vendor/angularjs/angular-mocks.1.2.25"],
			underscore: ["../../../../vendor/underscore/underscore"],
			lodash: ["../../../../vendor/lodash/lodash"],
			classjs: ["../../../../vendor/classjs/Class"],
			angularTreeView: ["../../../../vendor/angular-treeview/treeView"],
			angularData: ["../../../../vendor/angular-data/angular-data"],
			dxTree: ["../../../../vendor/dxtree/dxTree"],
			lessjs: ["../../../../vendor/lessjs/less.min.js"],
			bootstrapTree: ["../../../../vendor/bootstrap-tree/bootstrap-tree"],
			ngAside: ["../../../../vendor/angular-aside/js/angular-aside"],
			iarouseNav: ["../../../../vendor/iarouse/js/shared/Nav"],
			angularGridster: ["../../../../vendor/angular-gridster/angular-gridster"],
			angularAutolayout: ["../../../../vendor/angular-autolayout/angular-autolayout"],
			uiLayout: ["../../../../vendor/ui-layout/ui-layout"]
	
		},
		shim: {	
			jquery: {
				exports: "$"
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
			nguirouter: {
				deps: ["ng"]
			},
			q: {
				deps: ["ng"],
				exports: "q"
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
			dxTree: {
				deps: ["ng"]
			},
			bootstrapTree: {
				deps: ["ng", "jquery"]
			},
			ngAside: {
				deps: ["ng", "uibootstrap"]
			},
			iarouseNav: {
				deps: ["ng", "uibootstrap"]
			},
			angularGridster: {
				deps: ["ng", "jquery"]
			},
			angularAutolayout: {
				deps: ["ng"]
			},
			uiLayout: {
				deps: ["ng"]				
			}
			
		}
	});
	
	requirejs(["ng", "appjs"], function(ng, app){
		ng.element(document).ready(function(){
			ng.bootstrap(document, [app.name]);
		});
	});
	
}());