"use strict";
define(["angular", "mev-scatter-plot",
"../data/dataset_lgg2.json",
"../data/limma_result_lgg2.json",
], 
function(ng, mevScatterPlot, lgg2json, limmaJson){

	return ng.module("demo", arguments, arguments)
	.controller("myCtrl", ["$scope", function($scope){
		var _self = this;
		$scope.data = "random";
		$scope.vm={
			scatterPlotConfig: {
				name: "scatter-plot-demo"
			},
			logScaleX: false,
			logScaleY: false,
			zoomEnabled: false,
			selections: [{
				"name": "s3",
				"properties": {
					"selectionColor": "#14bdf8",
					"selectionDescription": ""
				},
				"keys": ["TCGA-E1-5302-01A-01R-1470-07", "TCGA-CS-5396-01A-02R-1470-07", "TCGA-DB-5281-01A-01R-1470-07", "TCGA-DH-5141-01A-01R-1470-07", "TCGA-CS-4944-01A-01R-1470-07", "TCGA-DH-5144-01A-01R-1470-07"]
				}, {
					"name": "s2",
					"properties": {
						"selectionColor": "#dd4543",
						"selectionDescription": ""
					},
					"keys": ["TCGA-E1-5302-01A-01R-1470-07", "TCGA-CS-5396-01A-02R-1470-07", "TCGA-DB-5281-01A-01R-1470-07", "TCGA-E1-5311-01A-01R-1470-07", "TCGA-DB-5274-01A-01R-1470-07", "TCGA-DH-5143-01A-01R-1470-07", "TCGA-DB-5273-01A-01R-1470-07", "TCGA-DH-5140-01A-01R-1470-07"]
				}, {
					"name": "s1",
					"properties": {
						"selectionColor": "#d3c0c3",
						"selectionDescription": ""
					},
					"keys": ["TCGA-DB-5280-01A-01R-1470-07", "TCGA-DH-5142-01A-01R-1470-07", "TCGA-DB-5278-01A-01R-1470-07", "TCGA-DH-5141-01A-01R-1470-07", "TCGA-CS-4944-01A-01R-1470-07", "TCGA-DH-5144-01A-01R-1470-07"]
				}],
			dataset: lgg2json,
			limma: limmaJson
		};
	}]);

    
});
