"use strict";
define(["steal-jasmine", "mev-scatter-plot", "angular-mocks", 
	"../../../data/pca_result_lgg.json",
	"../../../data/limma_result_lgg2.json",
	"../../../data/dataset_lgg2.json"
	], 
function(jsamineRequire, mevScatterPlot, ngMock, pcaJson, limmaJson, lggJson){
	describe("Mev Scatter Plot tests", function(){
		
		beforeEach(module("ngMock"));
		beforeEach(module(mevScatterPlot.name));

		var Nvd3DataAdaptor;
		beforeEach(inject(function(mevNvd3DataAdaptor){
			Nvd3DataAdaptor = mevNvd3DataAdaptor;
		}));

		it("transform data from an object map to nvd3 input format", function(){			
			var data = Nvd3DataAdaptor.transform(pcaJson.x, "PC1", "PC2", null, [], 10);
			console.log("pca", data);			
			expect(data.length).toBe(1);			
			expect(data[0].values.length).toBe(10);
			expect(data[0].values[0].x).toEqual(-57.9632);
			expect(data[0].values[0].y).toEqual(41.3324);
			expect(data[0].values[0].id).toEqual("TCGA-CS-4944-01A-01R-1470-07");

		});		
		it("transform data from an object array to nvd3 input format", function(){			
			var data = Nvd3DataAdaptor.transform(limmaJson.results, "logFoldChange", "averageExpression", "id", [], 10);
			console.log("limma", data);			
			expect(data.length).toBe(1);			
			expect(data[0].values.length).toBe(10);
			expect(data[0].values[0].x).toEqual(-2.5561);
			expect(data[0].values[0].y).toEqual(2.3358);
			expect(data[0].values[0].id).toEqual("TLR7");

		});		
		it("transform data from an object array to nvd3 input format with selections", function(){			
			var data = Nvd3DataAdaptor.transform(limmaJson.results, "logFoldChange", "averageExpression", "id", lggJson.row.selections, 1000);
			console.log("limma", data);			
			expect(data.length).toBe(2);			
			expect(data[1].values.length).toBe(5);
			expect(data[1].values[0].x).toEqual(-2.5561);
			expect(data[1].values[0].y).toEqual(2.3358);
			expect(data[1].values[0].id).toEqual("TLR7");

		});		
		it("forEech object and array in the same way", function(){			

			var arr = [{id: "a", x: 1, y: 1}, {id: "b", x: 2, y: 2}, {id: "c", x: 3, y: 3}];			
			var obj = {a: {x: 1, y: 1}, b: {x: 2, y: 2}, c: {x: 3, y: 3}};

			var result = [];
			var count=0;
			var fn = function(item, id){
				if(count++>1) return false;
				result.push(id);
			};
			Nvd3DataAdaptor.forEachById(arr, fn, "id");
			var aResult = result;
			result = [];
			count = 0;
			Nvd3DataAdaptor.forEachById(obj, fn);

			expect(aResult).toEqual(["a", "b"]);
			expect(result).toEqual(["a", "b"]);
			expect(result).toEqual(aResult);
			expect(result).not.toBe(aResult);
			result = [];
			count = 0;
			Nvd3DataAdaptor.forEachById(obj, fn, "x");						
			expect(result).toEqual([1, 2]);
		});		
	});
});
