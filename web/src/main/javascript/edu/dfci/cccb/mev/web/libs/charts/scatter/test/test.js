"use strict";
define(["steal-jasmine", "mev-scatter-plot", "crossfilter", 
	"./services/adaptors/TestNvd3DataAdaptor.spec"], 
function(jsamineRequire, mevScatterPlot, crossfilter){

	function generateData(groups, points) {
		var data = [],
//	                shapes = ['circle', 'cross', 'triangle-up', 'triangle-down', 'diamond', 'square'],
			shapes = ['circle'],
			random = d3.random.normal();

		for (var i = 0; i < groups; i++) {
			data.push({
				key: 'Group ' + i,
				values: []
			});
			for (var j = 0; j < points; j++) {
				data[i].values.push({
					x: random() * 1000
					, y: random() * 1000
					, size: Math.random() 
					, shape: shapes[j % 6]
				});
			}
		}
		// data.push({key: "Copy 0", values: []});
		// data[0].values.map(function(item){				
		// 	data[data.length-1].values.push(item);
		// });
		return data;
	};


	describe("Mev Scatter Plot tests - playing with lodash", function(){
		it("ensure mevScatterPlot is an AngularJS module", function(){
			console.debug("mevScatterPlot", mevScatterPlot);
			expect(true).toBe(true);
		});		

		it("ensure crossfilter works", function(){
			var data = generateData(2, 3);
			var values = _.flatten(_.map(data, function(series){
				return series.values;
			}));
			console.log("values", values);
			expect(true).toBe(true);
		});		


		it("find out how forEach works with array", function(){
			var arr = [{x: 1, y: 1}, {x: 2, y: 2}, {x: 3, y: 3}];
			var count=0;
			var arrMap = [];
			_.forEach(arr, function(item, index){											
				console.log("count", count);				
				if(count++>1) return false;
				arrMap.push(item.x)
			})
			console.log("arrMap", arrMap);	
			expect(arrMap.length).toBe(2);
			expect(arrMap).toEqual([1, 2]);
		});		
		it("find out how forEach works with object", function(){
			var arr = {a: {x: 1, y: 1}, b: {x: 2, y: 2}, c: {x: 3, y: 3}};
			var count=0;
			var arrMap = [];
			_.forEach(arr, function(item, key){											
				console.log("count", count);				
				if(count++>1) return false;
				arrMap.push(key)
			})
			console.log("arrMap", arrMap);	
			expect(arrMap.length).toBe(2);
			expect(arrMap).toEqual(["a", "b"]);
		});		
	});
});
