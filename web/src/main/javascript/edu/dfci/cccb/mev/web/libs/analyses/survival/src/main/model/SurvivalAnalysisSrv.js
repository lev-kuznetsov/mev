define(["lodash"], function(_){"use strict";
	var SurvivalAnalysisSrv=function(mevAnnotationRepository){
		function isNumber(n) {
			  return !isNaN(parseFloat(n)) && isFinite(n);
		}
		
		this.getInputDataTcga=function(params){
			// var ClinicalSummaryRepository = MevClinicalSummaryRepositorySrvc.create();
			var annotations = new mevAnnotationRepository("column");
			return annotations.getDataTable(["days_to_death", "days_to_last_followup", "vital_status"]).then(function(data){
				// console.debug("SurvivalAnalysisSrv inputData", data);
				var results=[];
				if (data && data.length>0){
					if(!data[0].days_to_death)
						throw "Missing required annotation field \"days_to_death\"";
					if(!data[0].days_to_last_followup)
						throw "Missing required annotation field \"days_to_last_followup\"";
				}else{
					throw "Clinical annotations not loaded";
				}
				data.map(function(item){
					//mark items: experiment=1; control=0
					if(params.experiment.keys.indexOf(item.key) >= 0)
						item.group=2;
					else if(params.control.keys.indexOf(item.key) >= 0)
						item.group=1;					
					
					//only return items that are in one of the selected groups
					if(_.isNumber(item.group)){
						item.days_to_death = parseFloat(item.days_to_death);
						item.days_to_last_followup = parseFloat(item.days_to_last_followup);
						item.status = isNumber(item.days_to_death) ? 1 : 0;
						item.time = isNumber(item.days_to_death) ? item.days_to_death : item.days_to_last_followup;
						results.push(item);
					}					 
				});
				return results;
			});
		};
	};
	SurvivalAnalysisSrv.$inject=["mevAnnotationRepository"];
	SurvivalAnalysisSrv.$name="mevSurvivalAnalysisSrv";
	SurvivalAnalysisSrv.$provider="service";
	return SurvivalAnalysisSrv;
});