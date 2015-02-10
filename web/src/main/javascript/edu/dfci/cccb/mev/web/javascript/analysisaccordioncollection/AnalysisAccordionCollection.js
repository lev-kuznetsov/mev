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
    
    var serviceDeps = [
        './Services/tableResultsFilter',
        './Services/projectionService',
        './Services/pathService'
    ]

    define(deps.concat(contentDeps).concat(serviceDeps), function(angular, jq, d3){
	
        var module = angular.module('Mev.AnalysisAccordionCollection', ['Mev.AlertService'])

        //load each directive deps file onto the module using function arguments only
        for (index in directivesDeps){
            arguments[index + deps.length()](module)
        }
        
        //load each service deps file onto module
        for(index in serviceDeps){
            arguments[index + deps.length() + directiveDeps.length()](module)
        }
        
        return module
    
    })

})()