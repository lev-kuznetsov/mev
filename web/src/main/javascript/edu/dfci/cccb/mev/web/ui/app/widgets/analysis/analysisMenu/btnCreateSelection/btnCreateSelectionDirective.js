define([], function(){
	"use strict";
	var BtnCreateSelectionDirective = function BtnCreateSelectionDirective(alertService){
		function BtnCreateSelectionVM(scope){
			var self = this;			
			this.target = scope.muiTarget;
			this.analysis = scope.muiAnalysis;
			this.dataset = scope.muiDataset;
			this.items = scope.muiItems;
			this.key = scope.muiKeyName || "id";
			this.getId = function(){
				return "selectionAdd" + self.analysis.name + self.target; 
			};
			this.selectionParams={};
			this.addSelections = function (filteredResults) {
				
                var keys = filteredResults.map(function(item){return item[self.key];});                
                var selectionData = {
                    name: self.selectionParams.name,
                    properties: {
                        selectionDescription: '',
                        selectionColor: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16)
                    },
                    keys: keys
                };

                self.dataset.selection.post({
                        datasetName: self.dataset.datasetName,
                        dimension: "row"

                    }, selectionData,
                    function (response) {
                        self.dataset.resetSelections('row');
                        var message = "Added " + self.selectionParams.name + " as new Selection!";
                        var header = "Heatmap Selection Addition";

                        alertService.success(message, header);
                    },
                    function (data, status, headers, config) {
                        var message = "Couldn't add new selection. If " + "problem persists, please contact us.";

                        var header = "Selection Addition Problem (Error Code: " + status + ")";

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
				muiKeyName: "@"
			},
//			template: "<a class=\"btn\" data-target=\"#{{vm.getId()}}\" data-toggle=\"modal\"></i> Create Selections</a>",
			templateUrl: "app/widgets/analysis/analysisMenu/btnCreateSelection/btnCreateSelection.tpl.html",
		    link: function(scope, elem, attrs){
		    	scope.vm = new BtnCreateSelectionVM(scope);
		    }
		};
	}; 
	BtnCreateSelectionDirective.$name = "BtnCreateSelectionDirective";
	BtnCreateSelectionDirective.$inject = ["alertService"];
	return BtnCreateSelectionDirective;
});