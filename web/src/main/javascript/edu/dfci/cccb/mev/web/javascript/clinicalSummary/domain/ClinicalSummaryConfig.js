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
		field: "days_to_initial_pathologic_diagnosis",
		dataType: "numeric",
		chartType: "histogram"
	},{
		field: "days_to_last_followup",
		dataType: "numeric",
		chartType: "histogram"
	},{
		title: "ICD 10",
		field: "icd_10",
		dataType: "text",
		chartType: "piechart"
	},{
		field: "race",
		dataType: "text",
		chartType: "piechart"
	},{		
		field: "history_of_neoadjuvant_treatment",
		dataType: "text",
		chartType: "piechart"
	},{
		field: "person_neoplasm_cancer_status",
		dataType: "text",
		chartType: "piechart"
	},{
		field: "gender",
		dataType: "text",
		chartType: "piechart"
	},{
		field: "histological_type",
		dataType: "text",
		chartType: "piechart"
	},{
		field: "year_of_initial_pathologic_diagnosis",
		dataType: "text",
		chartType: "piechart"
	},{
		field: "vital_status",
		dataType: "text",
		chartType: "piechart"
	},{
		field: "ethnicity",
		dataType: "text",
		chartType: "piechart"
	},{
		field: "tissue_source_site",
		dataType: "text",
		chartType: "piechart"
	}
	];
	return configs;
});