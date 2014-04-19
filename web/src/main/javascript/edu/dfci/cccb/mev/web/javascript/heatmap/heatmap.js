define(['jquery', 'angular', 'heatmap/behaviors', 'extend',
        'notific8', 'api/api'], function($, angular, behaviors, extend) {
	return angular
		.module('Mev.heatmap', ['Mev.api'])
		.value('Heatmap.availableColors', ["Green-Black-Red",
                                           "Yellow-Black-Blue",
                                           "Red-White-Blue"])
        .service('Heatmap.saveCluster', [function(){
        	
        	return function(cluster) {

	        	var svg = d3.select("#svgWrapperHclTree_"+cluster.name).select("svg");
	        	 var html = svg
	        	 .attr("version", 1.1)
	             .attr("xmlns", "http://www.w3.org/2000/svg")
	             .node().parentNode.innerHTML;
	        	  
	        	 var imgsrc = 'data:image/svg+xml;base64,'+ btoa(html);
	//           var img = '<img src="'+imgsrc+'">';
	//           d3.select("#svgdataurl").html(img);
	        	  
	        	  
	        	 var canvas = document.querySelector("#canvasHclTree_"+cluster.name);
	        	 var context = canvas.getContext("2d");
	        	 canvas.width=svg.attr('width');
	        	 canvas.height=svg.attr('height');
	        	 
	        	 var image = new Image;
	        	 image.src = imgsrc;
	        	 image.onload = function() {
	        		                                         		 
	            	 canvas.style.opacity = 1;
	            	 context.beginPath();
	                 context.rect(0, 0, canvas.width, canvas.height);                                                 
	            	 context.fillStyle ="#FFFFFF";
	            	 context.fill();
	            	 
	            	 context.drawImage(image, 0, 0);
	            	 
	            	 canvas.toBlob(function(blob) {
	            		    saveAs(blob, cluster.name+".png");
	            	 });
//	                 var canvasdata = canvas.toDataURL("image/png");
//	                 var pngimg = '<img src="'+canvasdata+'">';
//	                 d3.select("#pngdataurl").html(pngimg);
//	                 var a = document.createElement("a");
//	                 a.download = "sample.png";
//	                 a.href = canvasdata;
//	                 a.click();
	            };
        	};
        }])
		.service('Heatmap.cellsFilter', [function(){
			
			return function(oldcells, rowLabels, colLabels, filterLabels){
				//Used to filter cells array assuming its an array in row major order  
                    
					//get indexes of filter labels from all labels
					var indexes = filterLabels.map(function(d){
                        return rowLabels.indexOf(d);
                    });
                    
                    var cells = []
                    //get rows from cells using indexes
                    indexes.map(function(index){
                       //get row by slicing using index
                       var row = this.oldcells.slice(index* colLabels.length, colLabels.length*(1+index))
                       //push rows onto cells
                       row.map(function(cell){
                           cells.push(cell)
                       });
                    });
                    
                    return cells
                    
			};
		}])
		.factory('heatmapDataset', ['alertService',
		    'api.dataset', 'api.dataset.analysis',
		    'api.dataset.selections', 
		    function(alertService, apiDataset, apiAnalysis, apiSelections){
			var dataset =  {
        		cells: {
        			avg: undefined,
					max: undefined,
					min: undefined,
					values: undefined
        		},
        		analysisList: {
        			hierarchical: [],
        			limma: [],
        			kMeans: [],
        			tTest: [],
        			anova: []
        		},
        		selections:{
        			row: undefined,
        			column: undefined
        		},
        		labels: {
        			row: undefined,
        			column:undefined
        		},
        		view : {
            		cells: {
            			avg: undefined,
    					max: undefined,
    					min: undefined,
    					values: undefined
            		},
            		labels:{
            			row: undefined,
            			column:undefined
            		},
            		panel:{
            			side:undefined,
            			top:undefined
            		},
            		color: undefined //setting default color
            	}
        	};
			
			extend(dataset, behaviors);
			Object
				.defineProperty(dataset.view, 'datasetViewFilter', 
						{value:behaviors.datasetViewFilter,
						 enumerable: true,
						 writable:false,
						 configurable: false})
        	
        	//Add some AngularJS modules to the dataset
        	Object.defineProperty(dataset, 'apiAnalysis', 
        			{value: apiAnalysis, 
        			 enumerable: true,
        			 configurable: false,
        			 writeable: false
        			}); 
        	
        	Object.defineProperty(dataset, 'apiSelections', 
        			{value: apiSelections, 
        			 enumerable: true,
        			 configurable: false,
        			 writeable: false
        			});
        	
        	Object.defineProperty(dataset, 'apiDataset', 
        			{value: apiDataset, 
        			 enumerable: true,
        			 configurable: false,
        			 writeable: false
        			});
        	
        	Object.defineProperty(dataset, 'alertService', 
        			{value: alertService, 
        			 enumerable: true,
        			 configurable: false,
        			 writeable: false
        			}); 
			
			return dataset;
			
			
		}])
		.controller('HeatmapCtrl', [
		'$scope', 
		'$routeParams', 
		'pseudoRandomStringGenerator',
        '$location',
        'heatmapDataset',
        'Heatmap.availableColors',
        function($scope, $routeParams, prsg,  $loc, heatmapDataset,
        		availableColors) {
			
			//case where there's no datasetName
			if (!$routeParams.datasetName) {
				$('#loading').modal('hide');
				$loc.path('/datasets'); //return back to datasets
				return
			};
        	
        	$scope.availableColors = availableColors;
        	
        	$scope.dataset = Object.create(heatmapDataset);
        	
        	//Initialize page
        	
        	$scope.dataset.setDataset();
        	//$scope.dataset.setDatasetAnalysisList();
        	//$scope.dataset.setDatasetSelections();
        	
			function addKMeansToPanel (inputData, panelType){
            
                var keys = [];
                for (var i=0; i<inputData.clusters.length; i++){
                    for (var j=0; j<inputData.clusters[i].length; j++){
                        keys.push(inputData.clusters[i][j])
                    }
                }
                inputData.keys = keys;
                
                $scope.view.panel[panelType] = inputData;
                $scope.view.labels[dimension] = inputData.keys;
                
            };
            
            function addHierarchicalToPanel(analysis, panelType){
            	$scope.view.panel[panelType] = analysis;
                $scope.view.labels[dimension] = analysis.keys;
            };
			
			function addToSidePanel(analysis){
			//take analysis and add to heatmap side panel
				if (analysis.type == "Hierarchical Clustering"){
                    addHierarchicalToPanel(analysis, "side")
                } else if (inputData.type == "K-means Clustering") {
                    addKMeansToPanel(analysis, "side")
                }
			};
			
			function addToTopPanel(analysis){
			//take analysis and add to heatmap top panel
				if (analysis.type == "Hierarchical Clustering"){
					addHierarchicalToPanel(analysis, "top")
                } else if (inputData.type == "K-means Clustering") {
                	addKMeansToPanel(analysis, "top")
                }
			};
			
        	
        }]);
});