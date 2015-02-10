(function(){

  define([], function(){

    return function(module){

        module.directive('fTestAccordion', ['pathService', function(paths){
    	    return {
                restrict : 'E',
                scope : {
            	    analysis : "=analysis",
            	    project : "=project"
                },
                templateUrl : paths.module + '/templates/fTestAccordion.tpl.html',
                link : function(scope) {
                    return
                }	
            }
        }])

        return module

    }

  })

})()
