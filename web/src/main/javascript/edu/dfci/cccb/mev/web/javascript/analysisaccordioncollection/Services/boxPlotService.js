(function(){
    
	define(["lodash"], function(_){
        return function(module){
            module.service('BoxPlotService', ["$q", function($q){
            	
                
            	this.prepareBoxPlotData=function(dataset, genes, selections, randomId, keyColumnName){
            		var max = Number.NEGATIVE_INFINITY, min = Number.POSITIVE_INFINITY;
            		var key = keyColumnName || "id";
            		function test(d) {
    	                if (d.value > max) {
    	                    max = d.value;
    	                };
    	                if (d.value < min) {
    	                    min = d.value;
    	                };	
    	            };
    	                	            
    	            var coords = [];
    	            genes.map(function(gene, i){
    	            	selections.map(function(selection){
    	            		if(typeof selection === "string")
    	            			selection = _.find(dataset.column.selections, {name: selection}); 
    	            		selection.keys.map(function(sampleId){
    	            			return coords.push({
    	            				row: gene[key],
    	            				column: sampleId
    	            			});
    	            		});
    	            	});    	            	
    	            });
    	            
    	            return dataset.expression.getDict(coords).then(function(dict){
    	            	return {
                            "data": genes.map(function (gene, i) {
                                var retGene = {                            	
                                    'geneName': gene[key],
                                    'pValue': gene.pValue,
                                    'groups': {}
                                };
                                
                                selections.map(function(selection){
                                	if(typeof selection === "string")
                                		selection = _.find(dataset.column.selections, {name: selection}); 
                                	retGene.groups[selection.name] = {                        			
                                        'values': selection.keys.map(function (label) {
                                             var datapoint = dict[gene[key]][label];
                                             test(datapoint, max, min);
                                             return datapoint;
                                        }),
                                        'color': selection.properties.selectionColor,
                                        'name': selection.name
                            		};
                            	});
                                
                                return retGene;
                            }),
                            'min': min - ((max - min) * .05),
                            'max': max + ((max - min) * .05),
                            'id': randomId
                        };
    	            })["catch"](function(e){
            			console.error("ERROR", e);    	            	
    	            });
    	                        		
            	};
            	
            }]);
        };
    });
    
})();