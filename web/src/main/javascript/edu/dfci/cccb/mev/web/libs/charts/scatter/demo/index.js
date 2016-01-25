define(["angular", "./demo"], function(ng, demo){	
	var xyza;
	ng.element(document).ready(function(){
        ng.bootstrap(document, [demo.name]);
    });
});
