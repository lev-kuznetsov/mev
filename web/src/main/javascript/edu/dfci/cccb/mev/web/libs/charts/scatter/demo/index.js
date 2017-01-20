define(["angular", "./demo", "mev-bootstrap-theme"], function(ng, demo){
	var xyza;
	ng.element(document).ready(function(){
        ng.bootstrap(document, [demo.name]);
    });
});
