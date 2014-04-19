define(['jquery', 'angular', 'notific8', 'api/api'], function($, angular) {

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
		.controller('HeatmapCtrl', [
		'$scope', 
		'$routeParams', 
		'pseudoRandomStringGenerator',
        '$location',
        'alertService',
        'api.dataset',
        'api.dataset.analysis',
        'api.dataset.selections',
        'Heatmap.availableColors',
        'Heatmap.cellsFilter',
        function($scope, $routeParams, prsg,  $loc, alertService, 
        apiDataset, apiAnalysis, apiSelections, availableColors, cellsFilter) {
			
			//case where there's no loaded dataset
			if (!$routeParams.datasetName) {
				$('#loading').modal('hide');
				$loc.path('/datasets'); //return back to datasets
				return
			};
        	
        	$scope.availableColors = availableColors;
        	
        	$scope.dataset = {
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
        		}
        	};
        	
        	$scope.heatmapView = {
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
        		color: $scope.availableColors[0] //setting default color
        	};
        	
        	//Scope available behaviors
        	$scope.behaviors = {
        		resetHeatmap: setHeatmapViewToDefault,
        		setHeatmap: setHeatmapView,
        		addSelection: addNewSelection,
        		addAnalysis: addNewAnalysis,
        		addAnalysisToSidePanel: addToSidePanel,
        		addAnalysistoTopPanel: addToTopPanel
        	};

        	
        	//Initialize page
        	function init(){
        		setDataset();
        	};
        	
        	init();
        	
        	function addNewAnalysis(params){
            	//function to add new analysis to the dataset
        			//
            		postData = {
        	        	name: params.name,
        	        };
            		
            		//send post request for new analysis
            		apiAnalysis.post({analysisType:params.type}, postData,
            		function(response){
            		//on success, reset analysis list with new analysis added
            			setDatasetAnalysisList()
            		}, function(error){
            			var message = "Could not start new analysis " + params.name
	   					     + ". If the"
	   					     + "problem persists, please contact us.";
	   					
	   					var header = "Analysis start Problem (Error Code: "
	   					         + error.status
	   					         + ")";
	   					
	   					alertService.error(message, header);
            		})
        	};
        	
        	function addNewSelection(params){
        	//function to add new selection to the dataset
        		postData = {
    	        	name: params.name,
    	        	properties: {
    	        		selectionDescription: '',
    	        		selectionColor: params.color,                     
    	        	},
    	            keys: params.keys
    	        };
        		
        		//send post request for selections
        		apiSelections.post({dimension:params.dimension}, postData,
        		function(response){
        		//on success, reset selections with new selection added
        			setDatasetSelections();
        		}, function(error){
					var message = "Could not add new selection"
					     + ". If the"
					     + "problem persists, please contact us.";
					
					var header = "Selections Addition Problem (Error Code: "
					         + error.status
					         + ")";
					
					alertService.error(message, header);
        		});
        	};
        	
        	function clearAnalysisList(){
        	//function to clear analysis list to undefined state for reset
            	$scope.dataset.analysisList= {
        			hierarchical: [],
        			limma: [],
        			kMeans: [],
        			tTest: [],
        			anova: []
        		}
        	};
        	
        	function setDataset(){
        	//Pull original dataset from http and set the dataset
        		$('#loading').modal();
        		//http request data
        		
        		apiDataset.get(function(result){
        			$('#loading').modal('hide');
	            //set cells
        			$scope.dataset.cells.avg = result.avg;
        			$scope.dataset.cells.max = result.max;
        			$scope.dataset.cells.min = result.min;
        			$scope.dataset.cells.values = result.values;
        			
    			//set labels
					$scope.dataset.labels.row = result.row.keys;
					$scope.dataset.labels.column =result.column.keys;
						
	            }, function(error){
	            	 var message = "Could not retrieve dataset "
	                     + $routeParams.datasetName
	                     + ". If the"
	                     + "problem persists, please contact us.";
	
	                 var header = "Heatmap Download Problem (Error Code: "
	                         + error.status
	                         + ")";
	
	                 alertService.error(message, header);
	                 
	                 $('#loading').modal('hide');
	                 $loc.path('/datasets');
	            });
        		
        		//set analysis list
        		setDatasetAnalysisList()
        		
        		//set selections
				setDatasetSelections()
        		
        	};
        	
        	function setDatasetSelections(){
        	//Sets dataset selections for both dimentions
        		setDatasetSelectionsRow();
        		setDatasetSelectionsColumn();
        	};
        	
        	function setDatasetSelectionsRow(){
        		apiSelections.getAll({dimension:'row'},
				function(response){
					$scope.dataset.selections.row = response.selections;
				},function(error){
					var message = "Could not retrieve dataset selections"
	                     + ". If the"
	                     + "problem persists, please contact us.";
	
	                 var header = "Selections Download Problem (Error Code: "
	                         + error.status
	                         + ")";
	
	                 alertService.error(message, header);
				});
        	};
        	
        	function setDatasetSelectionsColumn(){
        		apiSelections.getAll({dimension:'column'},
				function(response){
					$scope.dataset.selections.column = response.selections;
				},function(error){
					var message = "Could not retrieve dataset selections"
	                     + ". If the"
	                     + "problem persists, please contact us.";
	
	                 var header = "Selections Download Problem (Error Code: "
	                         + error.status
	                         + ")";
	
	                 alertService.error(message, header);
				});
				
				
        	};	
        	
        	function setHeatmapView(rowlabels){
        	//sets heatmap view with given row labels
        		//filter cells on rowlabels
        		$scope.heatmapView.cells.values = cellsFilter($scope.dataset.cells.values,
    					$scope.dataset.labels.row, 
    					$scope.dataset.labels.column, rowLabels);
        		
        		//set rowlabels as heatmap view row labels
        		$scope.heatmapView.labels.row = rowLabels;
        		
        	};
        	
        	function setHeatmapViewToDefault(){
        	//reset Heatmap View with original dataset
        		setHeatmapView($scope.dataset.labels.row);
        	};
        	
        	function addToAnalysisList(data){
        	//Adds analysis http response to correct analysisList group
        		var randstr = prsg(5);
                var randstr2 = prsg(5);

                if (data.type == "Hierarchical Clustering") {

                    data.href = "#" + randstr;
                    data.parentId = randstr2;
                    data.dataParent = '#' + randstr2;
                    data.divId = randstr;
                    $scope.dataset.analysisList.hierarchical
                            .push(data);

                } else if (data.type == "LIMMA Differential Expression Analysis") {

                    $scope.dataset.analysisList.limma
                            .push({
                                name : data.name,
                                href : "#"
                                        + randstr,
                                parentId : randstr2,
                                dataParent : '#'
                                        + randstr2,
                                divId : randstr,
                                datar : data
                            });
                        
                } else if (data.type == "K-means Clustering"){
                	$scope.dataset.analysisList.kMeans.push(data);

                } else if (data.type == "Anova Analysis") {
                	$scope.dataset.analysisList.anova.push(data);

                } else if (data.type == "t-Test Analysis"){                                                                                                    
                	$scope.dataset.analysisList.tTest.push(data);

                }
        	};
        		
        	function setDatasetAnalysisList(){
        	//reset analysis list
        		//get analysis list
        		apiAnalysis.getAll(function(response){
                	
        			
                    var prevList = response.names; 
                //clear stored analysis list
                    clearAnalysisList();
                //for each analysis
                    prevList.map(function(analysisName){
                    //get analysis data
                    	apiAnalysis.get({analysisName: analysisName},
                    	function(data){
                    //push analysis data to stored analysis list
                    		addToAnalysisList(data);
                    	}, function(error){
                    		var message = "Could not retrieve previous analysis " + analysisName
		   	                     + ". If the problem persists, please contact us.";
		   	
		   	                 var header = "Analysis Download Problem (Error Code: "
		   	                         + error.status
		   	                         + ")";
		   	
		   	                 alertService.error(message, header);
                    	});
                    	
                    
                    });
                    
                    
        		}, function(error){
        			var message = "Could not retrieve previous analysis list"
	                     + ". If the problem persists, please contact us.";
	
	                 var header = "Analysis List Download Problem (Error Code: "
	                         + error.status
	                         + ")";
	
	                 alertService.error(message, header);
        		});
        		
        			
			};
			

			function addKMeansToPanel (inputData, panelType){
            
                var keys = [];
                for (var i=0; i<inputData.clusters.length; i++){
                    for (var j=0; j<inputData.clusters[i].length; j++){
                        keys.push(inputData.clusters[i][j])
                    }
                }
                inputData.keys = keys;
                
                $scope.heatmapView.panel[panelType] = inputData;
                $scope.heatmapView.labels[dimension] = inputData.keys;
                
            };
            
            function addHierarchicalToPanel(analysis, panelType){
            	$scope.heatmapView.panel[panelType] = analysis;
                $scope.heatmapView.labels[dimension] = analysis.keys;
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