(function(){

  define([], function(){

    return function(module){

      module.directive('wilcoxonTestAccordion', ['pathService', function(paths){
        return {
            restrict : 'E',
            scope : {
            	analysis : "=analysis",
            	project : "=project"
            },
            templateUrl : paths.module + '/templates/wilcoxonTestAccordion.tpl.html',
            link : function(scope) {
            }	
        }
      }])

      return module

    }

  })

})()
