(function(){
    
    define([], function(){
        return function(module){
            module.service('BoxPlotService', [function(){
            	
                
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
    	            
            		return {
                        "data": genes.map(function (gene, i) {
                            var retGene = {                            	
                                'geneName': gene[key],
                                'pValue': gene.pValue,
                                'groups': {}
                            };
                            
                            selections.map(function(selection){
                            	retGene.groups[selection.name] = {                        			
                                    'values': selection.keys.map(function (label) {
                                         var datapoint = dataset.expression.get([gene[key], label]);
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
            	};
            	
            }]);
        };
    });
    
})();