define(["mui", "mev-mock", "./mock", "./mockService"], function(ng, mevMock, mock, mockVM, reload){	
	var app = ng.module("demo", arguments, arguments);
	var root = ng.element(document);
	root.ready(function(){	
		if(root.injector()){	
			root.data("$injector", null);
			angular.element("body > ui-view").removeData().empty();
			console.log("ng already bootstrapped, force re-init", root.injector());
		}
		ng.bootstrap(root, [app.name]);    
	});	
	// ng.element(document).ready(function(){
 //        ng.bootstrap(document, ["demo"]);
 //    });

	// reload(function(){
	// 	//window.location.reload(); 
	// 	// jasmine.getEnv();
	// 	console.debug("reload", arguments);   
	// });

	//reload("*", function(name, suite){
		// window.location.reload(); 
		// console.log("spec", "spec=mevPathwayEnrichment tests");
		// if(name.indexOf("@")>0)
		// 	window.location.search="spec="+suite.description;
		// window.location.search="spec=mevPathwayEnrichment tests"
		// jasmine.getEnv();
	//}); 	

	return app;
});
		