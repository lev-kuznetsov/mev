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
  	});

});