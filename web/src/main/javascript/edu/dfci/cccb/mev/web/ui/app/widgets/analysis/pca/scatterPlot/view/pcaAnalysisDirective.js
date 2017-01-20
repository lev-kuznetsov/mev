define(function(require){
	"use strict";
	var ng = require("angular");
	var _ = require("lodash");
	var d3 = require("d3");

	var PcaAnalysisDirective = function PcaAnalysisDirective(){

		function transformData(analysis, x, y, selections) {
		    
		    var selectedSets = _.filter(selections, function(s){return s.checked;});
		    var groups = {};
		    _.map(analysis.x, function(item, key){
		    	var groupAcc = {names: [], color: "grey"};
		    	_.map(selectedSets, function(selection){
		    		if(_.contains(selection.keys, key)){		    			
		    			groupAcc.names.push(selection.name);
		    			groupAcc.color = selection.properties.selectionColor;
		    		}
		    	});

		    	var groupName = "none";
		    	if(groupAcc.names.length===1)
		    		groupName = groupAcc.names[0];
		    	else if(groupAcc.names.length>1)
		    		groupName = groupAcc.names.join("+");

		    	var group = groups[groupName];
		    	if(!group){
		    		groups[groupName] = group = {};
			    	group.name = group.key = groupName;			    	 
			    	group.names = groupAcc.names;
			    	if(group.names.length<2){
			    		group.color = groupAcc.color;
			    	}else{
			    		group.color = '#'+Math.floor(Math.random()*16777215).toString(16);
			    	}	
			    	group.shape = 'circle';
			    	group.values=[];
		    	}
					    	
		    	group.values.push({
        			x: item[x],
        			y: item[y],
        			size: 10,	        			
        			sample: key,
        			id: key,	        			
            	});
		    });		    
			
			if(Object.keys(groups).length === 1)
				groups["none"].name = groups["none"].key = "Selection: none";

	        return _.sortBy(groups, function(group){
	        	return group.name === "none"  ? Infinity : group.names.length;	        		 
	        });
	    }

		return {
			restrict: "EAC",
//			template: require("./pcaAnalysis.tpl.html!"),
			templateUrl: "/container/ui/app/widgets/analysis/pca/scatterPlot/view/pcaAnalysis.tpl.html",
			scope: {
				pcaAnalysis: "=",
				selections: "=mevSelections",
				curSelection: "="
			},
			controller: ["$scope", function(scope){				
				scope.vm = {
					xLabel: "PC1",
					yLabel: "PC2",					
					updateAxis: function(){
						scope.vm.data = transformData(scope.pcaAnalysis, scope.vm.xLabel, scope.vm.yLabel, scope.selections);
					},
					updateSelection: function(){
						scope.vm.setData(transformData(scope.pcaAnalysis, scope.vm.xLabel, scope.vm.yLabel, scope.selections));			
					},
					setData: function(data){
						scope.vm.data = data;						
	        			scope.vm.selection = undefined;
					}
				};

				scope.$watch("pcaAnalysis", function(newVal){
					scope.vm.setData(transformData(newVal, scope.vm.xLabel, scope.vm.yLabel, scope.selections));	
				});

				scope.$on("mev.scatterPlot.selection", function($event, selection){
					scope.vm.selected = {
						items: selection,
						xLabel: scope.vm.xLabel,
						yLabel: scope.vm.yLabel
					};
					scope.curSelection = scope.vm.selected.items;
					console.debug("pca selection", scope.vm.bar);
				});

			}]
		};
	};

	PcaAnalysisDirective.$inject=[];
	PcaAnalysisDirective.$name="PcaAnalysisDirective";
	return PcaAnalysisDirective;
});