define(["mev-project/data/mouse_test_data.tsv.json",
	"mev-mock/data/deseq_test_data.tsv.json",
	"mev-mock/data/lgg.json",
	"mev-project"],
function(mouseJson, deseqJson, lggJson){ "use strict";
	var service = function(){
		this.datasetName = "mouse_test_data.tsv";
		this.datasets = {
			"mouse_test_data.tsv": mouseJson,
			"deseq_test_data.tsv": deseqJson,
			"lgg": lggJson
		}
		this.$get = ["mevProject", function service(mevProject){
			var project = mevProject(this.datasetName, this.datasets[this.datasetName]);
			return project;
		}];
	}

	service.$inject=[];
	service.$name="mevMockProject";
	service.$provider="provider";
	return service;
});