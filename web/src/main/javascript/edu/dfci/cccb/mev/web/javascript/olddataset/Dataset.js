define(['./lib/cellFilter', './lib/viewUpdate', 'd3', 'view/View'], 
function(cellFilter, viewUpdate, d3){

	return angular.module('Mev.Dataset', ['Mev.Api', 'Mev.View'])
		.factory('DatasetClass', 
		['api.dataset', 'api.dataset.analysis', 'api.dataset.selections', 'ViewClass'
		function(apiDataset, apiAnalysis, apiSelections){
		//Dataset constructor. params is an object with required parameters with form
		// params = {datasetName:(string), view:(boolean) }
		//	
		
			return function(params) {
				
				var reference = this;
				
				apiDataset.get({datasetName:datasetName},
			    function(result){
			    //set cells
					
					Object.defineProperty(reference, 'data', {
						value : {
							cells: {
								avg : result.avg,
								max : result.max,
								min : result.min,
								values : result.values,
								
							},
							labels : {
								row : result.row.keys,
								column : result.column.keys
							}, 
							selections : {
								row : result.row.selections,
								column : result.column.selections
							}
						}, 
						enumerable : true,
						writable :true,
						
					});
					
					var View = new ViewClass;
					
			    }, function(error){
			    	 var message = "Could not retrieve dataset info"
			             + ". If the"
			             + "problem persists, please contact us.";

			         var header = "Heatmap Download Problem (Error Code: "
			                 + error.status
			                 + ")";

			         alertService.error(message, header);
			         
			    });
				
				this.datasetName = datasetName;
			
				this.cellFilter = cellFilter;
			
				this.viewUpdate = viewUpdate;
				
				Object.defineProperty(reference, 'apiAnalysis', 
						{value: apiAnalysis, 
						 enumerable: true,
						 configurable: false,
						 writeable: false
						}); 
				
				Object.defineProperty(reference, 'apiSelections', 
						{value: apiSelections, 
						 enumerable: true,
						 configurable: false,
						 writeable: false
						});

				Object.defineProperty(reference, 'alertService', 
						{value: alertService, 
						 enumerable: true,
						 configurable: false,
						 writeable: false
						});
		};

	}]);
		
});

Object.defineProperty(reference, 'view', {
	value : {
		cells: {
			avg : result.avg,
			max : result.max,
			min : result.min,
			values : result.values,
			xScale : d3.scale.ordinal(),
			yScale : d3.scale.ordinal()
		},
		labels : {
			row : {
				values:result.row.keys
			},
			column : {
				values:result.column.keys,
			}
		}, 
		selections : {
			row : {
				values: result.row.selections,
				xScale : d3.scale.ordinal(),
    			yScale : d3.scale.ordinal(),
			},
			column : {
				values: result.column.selections,
				xScale : d3.scale.ordinal(),
				yScale : d3.scale.ordinal(),	
				
			}
		},
		panel: {
			top: {
				xScale : d3.scale.ordinal(),
    			yScale : d3.scale.ordinal().rangeRoundBands([0, 150], 0, 0),
			},
			side: {
				xScale : d3.scale.ordinal().rangeRoundBands([0, 150], 0, 0),
    			yScale : d3.scale.ordinal(),
			}
		}
	}, 
	enumerable : true,
	writable :true,
});