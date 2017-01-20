define(["mui", "lodash", "mev-dotplot", "./topgo_dummy.json"], function(ng, _, mevDotplot, topgoJson){"use strict";
	var app = ng.module("mev-dotplot-demo", arguments, arguments);

	app.controller('MainCtrl', function($scope) {
	  $scope.config = {
		  data: topgoJson.results,
		  series: {
			  label: "Ratio",
			  sort: {
				field: "significantGenes",
				order: "desc"
			  }
		  },
		  x: {
			  field: "goTerm"
		  },
		  y: {
			  field: function(d){
				  return d.significantGenes/d.annotatedGenes;
			  },
			  label: "Ratio",
			  precision: 0
		  },
		  size: {
			  field: "significantGenes",
			  label: "Match",
			  precision: 0
		  },
		  color: {
			  field: "pValue",
			  precision: 4
		  },
		  tooltip: {
			  title: function(config, item){
			  	return config.x.label + ": " + config.x.get(item.data);
			  },
			  fields: {
				  "Total": function(d){
					  return d.annotatedGenes;
				  }
			  }
		  }
	  };
	});

	ng.element(document).ready(function(){
		ng.bootstrap(document, [app.name]);
	});
});