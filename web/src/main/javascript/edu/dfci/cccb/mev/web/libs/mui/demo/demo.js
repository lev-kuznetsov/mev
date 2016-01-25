define(["mui", "./mock"], function(ng){	
	ng.element(document).ready(function(){
        ng.bootstrap(document, ["demo"]);
    });
	return ng.module("demo", arguments, arguments);
});
