define([], function(){
	"use strict";
	var BtnExportSelectionDirective = function BtnExportSelectionDirective(alertService){
		function BtnExportSelectionVM(scope){
			var self = this;			
			this.target = scope.muiTarget;
			this.analysis = scope.muiAnalysis;
			this.dataset = scope.muiDataset;
			this.items = scope.muiItems;
			this.key = scope.muiKeyName || "id";
			this.dimension = scope.muiDimension || "row";
			this.getId = function(){
				return "selectionExport" + self.analysis.name + self.target; 
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

                self.dataset.selection.export({
                        datasetName: self.dataset.datasetName,
                        dimension: self.dimension

                    }, selectionData,
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
				muiDimension: "@"
			},
//			template: "<a class=\"btn\" data-target=\"#{{vm.getId()}}\" data-toggle=\"modal\"></i> Create Selections</a>",
			templateUrl: "app/widgets/analysis/analysisMenu/btnExportSelection/btnExportSelection.tpl.html",
		    link: function(scope, elem, attrs){
		    	scope.vm = new BtnExportSelectionVM(scope);
		    }
		};
	}; 
	BtnExportSelectionDirective.$name = "BtnExportSelectionDirective";
	BtnExportSelectionDirective.$inject = ["alertService"];
	return BtnExportSelectionDirective;
});