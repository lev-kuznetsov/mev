define(["clinical/domain/ClinicalSummaryField"], function(ClinicalSumamryField){
	var ClinicalSummaryFactory = function(configs, repository){
	
		//public
		this.getAll=function(){
			var result = [];			
			for(var i=0;i<configs.length;i++){
				var curConfig = configs[i];
				var curFieldName = curConfig.field;
				result.push(new ClinicalSumamryField(configs[i], repository.getData([curFieldName])));
			}
			return result;
		};
	};
	
	return ClinicalSummaryFactory;
});