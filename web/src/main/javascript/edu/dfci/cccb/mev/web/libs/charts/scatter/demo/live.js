import angular from "angular" ;
import demo from "./demo";
import reload from "live-reload";

angular.element(document).ready(function(){
    angular.bootstrap(document, [demo.name]);
});

reload(function(){		
	console.debug("reload");
	angular.bootstrap(document, [demo.name]);	
});
