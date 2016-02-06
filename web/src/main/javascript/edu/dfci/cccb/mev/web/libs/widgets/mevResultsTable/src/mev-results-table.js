define(["mui", "angular-utils-pagination", 
	"./view/mevResultsTableDirective",
	"./view/mevResultsTableDefaults",
	"./view/mevResultsTableFilter",
	"./view/mevResultsTableDefaults",
	"./view/mevResultsTableCompareFactory",
	"./view/textOrNumberFilter",
	], 
function(ng){
	return ng.module("mevResultsTableModule", arguments, arguments)
	.filter('mevIsArray', function() {
	  return function (input) {
	    return ng.isArray(input);
	  };
  	})
  	.filter('textOrNumber', function ($filter) {
	    return function (input, fractionSize) {
	        if (isNaN(input)) {
	            return input;
	        } else {
	        	if(Math.abs(input)>1000)    	        		 
	        		return Number.parseFloat(input).toExponential(fractionSize);
	        	else
	        		return $filter('number')(input, fractionSize);
	        };
	    };
	});

});