define(function(require){
	"use strict";
	require("../style/pcaAnalysis.less");
	var ng = require("mui");
	var _ = require("lodash");
	var d3 = require("d3");

	var PcaAnalysisDirective = function PcaAnalysisDirective(){

		function transformData(analysis, x, y, selections) {
		    
		    var selectedSets = _.filter(selections, function(s){return s.checked;});
		    var groups = {};
		    _.map(analysis.x, function(item, key){
		    	var groupAcc = {names: [], color: "grey"};
		    	_.map(selectedSets, function(selection){
		    		if(_.includes(selection.keys, key)){
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
			template: require("./pcaAnalysis.tpl.html!"),
			scope: {
				pcaAnalysis: "=mevPcaAnalysis",
				selections: "=mevSelections",
				curSelection: "=",
				curGroups: "=mevCurGroups",
				curSelections: "=mevCurSelections"
			},
			controller: ["$scope", function(scope){				
				scope.vm = {
					xLabel: "PC1",
					yLabel: "PC2",
					logScaleX: false,
					logScaleY: false,
					dragAction: "select",
					isZoomEnabled: function(){
						return scope.vm.dragAction === "zoom";
					},
					updateAxis: function(){
						scope.vm.data = transformData(scope.pcaAnalysis, scope.vm.xLabel, scope.vm.yLabel, scope.selections);
					},
					updateSelection: function(){
						scope.vm.setData(transformData(scope.pcaAnalysis, scope.vm.xLabel, scope.vm.yLabel, scope.selections));			
					},
					setData: function(data){
						scope.vm.data = data;						
	        			scope.vm.selection = undefined;
					},
					updateScale: function(){
						scope.vm.setData();	
					},
					fields: ["PC1", "PC2", "PC3"]
				};

				scope.$watch("pcaAnalysis", function(newVal){
					if(newVal)
						scope.vm.setData(transformData(newVal, scope.vm.xLabel, scope.vm.yLabel, scope.selections));	
				});

				scope.$on("mev.scatterPlot.selection", function($event, selected){
					scope.curSelection.length = 0;
					selected.items.map(function(item){
						scope.curSelection.push(item);
					});
					console.debug("pca selection", scope.vm.bar);
				});
				scope.$on("mev.scatterPlot.groups.updated", function($event, groups){
					scope.curGroups.length = 0;
					groups.map(function(group){
						_.assign(group.selection, {
							name: group.selections.map(function(s){
								return s.name;
							}).join("+")
						});
						scope.curGroups.push(group.selection);
					});
					console.debug("pca groups", groups);
				})
				scope.$on("mev.scatterPlot.selections.updated", function($event, selections){
					scope.curSelections.length = 0;
					selections.map(function(selection){
						scope.curSelections.push(selection);
					});
					console.debug("pca selections", scope.curSelections);
				})
			}]
		};
	};

	PcaAnalysisDirective.$inject=[];
	PcaAnalysisDirective.$name="MevPcaAnalysisDirective";
	return PcaAnalysisDirective;
});