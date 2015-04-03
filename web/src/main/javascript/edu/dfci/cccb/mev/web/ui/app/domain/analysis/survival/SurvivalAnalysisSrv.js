define(["ng", "lodash"], function(ng, _){
	var SurvivalAnalysisSrv=function(MevClinicalSummaryRepositorySrvc){
		function isNumber(n) {
			  return !isNaN(parseFloat(n)) && isFinite(n);
		}
		
		this.getInputDataTcga=function(params){			
			var ClinicalSummaryRepository = MevClinicalSummaryRepositorySrvc.create();
			return ClinicalSummaryRepository.getDataTable(["days_to_death", "days_to_last_followup", "vital_status"]).then(function(data){				
				console.debug("SurvivalAnalysisSrv inputData", data);
				var results=[];
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
	SurvivalAnalysisSrv.$inject=["MevClinicalSummaryRepositorySrvc"];
	return SurvivalAnalysisSrv;
});