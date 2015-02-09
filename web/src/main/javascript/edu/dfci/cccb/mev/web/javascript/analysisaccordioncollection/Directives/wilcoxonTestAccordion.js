(function(){

  define([], function(){

    return function(module){

      module.directive('wilcoxonTestAccordion', [function(){
        return {
            restrict : 'E',
            scope : {
            	analysis : "=analysis",
            	project : "=project"
            },
            templateUrl : module.path + '/templates/wilcoxonTestAccordion.tpl.html',
            link : function(scope) {
            }	
        }
      }])

      return module

    }

  })

})()
