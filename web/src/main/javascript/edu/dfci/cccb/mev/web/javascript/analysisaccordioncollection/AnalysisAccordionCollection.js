(function(){

    var deps = [
		'angular', 
		'jquery', 
		'd3', 
		'alertservice/AlertService', 
		'scatterplot/ScatterplotModule'
		] 
    
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
      './Directives/pcaAccordion',
      './Directives/resultsTable',
      './Directives/nmfAccordion',
      './Directives/nmfHeatmap'
      ]
    
    var serviceDeps = [
        './Services/tableResultsFilter',
        './Services/projectionService',
        './Services/pathService',
        './Services/compareFactory',
        './Services/pcaTransforms',
        './Services/pcaMulti'
 
    ]

    define(deps.concat(directiveDeps).concat(serviceDeps), function(angular, jq, d3){
	
    	var moduleDeps = [
			'Mev.AlertService', 
			'Mev.ScatterPlotVisualization'
			]
        var module = angular.module('Mev.AnalysisAccordionCollection', moduleDeps)

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
