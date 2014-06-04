define([], function(){
	var configs=[{
		field: "age_at_initial_pathologic_diagnosis",
		dataType: "numeric",
		chartType: "histogram"
	},{
		field: "days_to_death",
		dataType: "numeric",
		chartType: "histogram"
	},{
		field: "icd_10",
		dataType: "text",
		chartType: "piechart"
	}
	];
	return configs;
});