define(["mui", "lodash"], function(ng, _){
	/* Random Data Generator (took from nvd3.org) */
    var service = function mevDotplotDataAdaptor(){
		return function(input){
            return input.map(function(item){
                return _.extend(item, {
                    getRation: function(){
                        return item.significantGenes / item.annotatedGenes;
                    },
                    getCount: function(){
                        return item.annotatedGenes;
                    }
                });
            });
        };
	};
	service.$name="mevDotplotDataAdaptor";
	service.$provider="factory";
	service.$inject=[];
	return service;
});