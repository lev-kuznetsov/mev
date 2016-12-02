define([], function(){
	"use strict";
	var BtnExportSelectionDirective = function BtnExportSelectionDirective(alertService, mevSelectionLocator){
		function BtnExportSelectionVM(scope){
			var self = this;			
			this.target = scope.muiTarget;
			this.analysis = scope.muiAnalysis;
			this.dataset = scope.muiDataset;
			this.items = scope.muiItems;
			this.key = scope.muiKeyName || "id";
			this.dimension = scope.muiDimension || "row";
			this.contextLevel = scope.contextLevel || "bottom";
			this.getId = function(){
				return ("selectionExport" + self.analysis.name + self.target).replace(/.*(?=#[^\s]+$)/, '').replace(/\./, '');;
			};
			this.exportParams={
					name: undefined,
                    color: '#ffffff'
			};
			this.exportSelection = function (filteredResults) {
                var keys = filteredResults.map(function(item){return item[self.key];});
                var selectionData = {
                    name: self.exportParams.name,
                    properties: {
                        selectionDescription: '',
                        selectionColor: self.exportParams.color,
                    },
                    keys: keys
                };
				
				var subsetData = {
					name: self.exportParams.name                    
                };
                subsetData[this.dimension+"s"] = keys;
                var otherDimension = this.dimension === "row" ? "column" : "row";
                var selections = mevSelectionLocator.find(otherDimension, scope.contextLevel);
                if(selections.length===1)
                	subsetData[otherDimension+"s"] = selections[0].keys;

                self.dataset.subset({
                    datasetName: self.dataset.datasetName
                }, subsetData,
                function (response) {
                	self.dataset.resetSelections(self.dimension);
                    var message = "Added " + self.exportParams.name + " as new Dataset!";
                    var header = "New Dataset Export";

                    alertService.success(message, header);
                },
                function (data, status, headers, config) {
                    var message = "Couldn't export new dataset. If " + "problem persists, please contact us.";

                    var header = "New Dataset Export Problem (Error Code: " + status + ")";

                    alertService.error(message, header);
                });

            };
		}
		return {
			restrict: "AE",
//			replace: true,
			scope: {
				muiTarget: "@",
				muiAnalysis: "=",
				muiDataset: "=",
				muiItems: "=",
				muiKeyName: "@",
				muiDimension: "@",
				contextLevel: "@mevContextLevel"
			},
//			template: "<a class=\"btn\" data-target=\"#{{vm.getId()}}\" data-toggle=\"modal\"></i> Create Selections</a>",
			templateUrl: "app/widgets/analysis/analysisMenu/btnExportSelection/btnExportSelection.tpl.html",
		    controller: ["$scope", function(scope){
		    	scope.vm = new BtnExportSelectionVM(scope);
		    }]
		};
	}; 
	BtnExportSelectionDirective.$name = "BtnExportSelectionDirective";
	BtnExportSelectionDirective.$inject = ["alertService", "mevSelectionLocator"];
	return BtnExportSelectionDirective;
});