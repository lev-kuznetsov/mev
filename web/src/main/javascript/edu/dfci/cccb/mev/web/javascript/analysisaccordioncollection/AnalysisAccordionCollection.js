(function(){

    var deps = ['angular', 'jquery', 'd3', 'alertservice/AlertService'] 
    
    var directiveDeps = [
      './Directives/analysisContentItem', 
      './Directives/fTestAccordion', 
      './Directives/wilcoxonTestAccordion',
      './Directives/kMeansAccordion',
      './Directives/anovaAccordion',
      './Directives/deseqAccordion',
      './Directives/tTestAccordion',
      './Directives/limmaAccordion',
      './Directives/hierarchicalAccordion',
      './Directives/resultsTable'
      ]
    
    var serviceDeps = [
        './Services/tableResultsFilter',
        './Services/projectionService',
        './Services/pathService',
        './Services/compareFactory'
    ]

    define(deps.concat(directiveDeps).concat(serviceDeps), function(angular, jq, d3){
	
        var module = angular.module('Mev.AnalysisAccordionCollection', ['Mev.AlertService'])

        //load each directive deps file onto the module using function arguments only
        for (var index = 0; index < directiveDeps.length; index++){
            arguments[index + deps.length](module)
        }
        
        //load each service deps file onto module
        for(var index = 0; index < serviceDeps.length; index++){
            arguments[index + deps.length + directiveDeps.length](module)
        }
        
        return module
    
    })

})()