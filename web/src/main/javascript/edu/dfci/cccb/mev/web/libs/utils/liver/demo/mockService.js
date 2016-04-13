define([], function(){
	var mevMockService = function mevMockService(mevMockProject){		
		this.getText=function(){
			return "mock text 15";
		};		
		this.getDatasetName=function(){
			return mevMockProject.dataset.id;
		};
		return this;
	};	
	mevMockService.$inject = ["mevMockProject"];
	mevMockService.$name="mevMockVM";
	mevMockService.$provider="service";
	return mevMockService;	
});