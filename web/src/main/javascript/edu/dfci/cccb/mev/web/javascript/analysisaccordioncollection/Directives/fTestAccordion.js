(function(){

  define([], function(){

    return function(module){

        module.directive('fTestAccordion', [function(){
    	    return {
                restrict : 'E',
                scope : {
            	    analysis : "=analysis",
            	    project : "=project"
                },
                templateUrl : module.path + '/templates/fTestAccordion.tpl.html',
                link : function(scope) {
                    return
                }	
            }
        }])

        return module

    }

  })

})()
