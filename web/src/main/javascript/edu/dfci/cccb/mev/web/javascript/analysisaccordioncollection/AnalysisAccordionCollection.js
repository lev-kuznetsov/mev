(function(){

    var deps = ['angular', 'jquery', 'd3', 'alertservice/AlertService'] 
    
    var directivesDeps = [
      './Directives/analysisContentItem', 
      './Directives/fTestAccordion', 
      './Directives/wilcoxonTestAccordion',
      './Directives/kMeansAccordion',
      './Directives/anovaAccordion',
      './Directives/deseqAccordion',
      './Directives/tTestAccordion',
      './Directives/limmaAccordion',
      './Directives/hierarchicalAccordion']


    define(deps.concat(contentDeps), function(angular, jq, d3){
	
        var module = angular.module('Mev.AnalysisAccordionCollection', ['Mev.AlertService'])
		
        module.path = "container/javascript/analysisaccordioncollection"

        //load each directive deps file onto the module using function arguments only
        for (index in directivesDeps){
            arguments[index + deps.length()](module)
        }
        
        return module
    
    })

})()