define(['./DatasetValues32', './DatasetValuesJson'], function(DatasetValues32, DatasetValuesJson){
	return function ValueStore(dataset, $http, $rootScope, datasetRespObj){
    	var self = this;    	
        var instance;
    	if(datasetRespObj && datasetRespObj.values.length > 0)
            instance = new DatasetValuesJson(dataset, $http, $rootScope);
        else
            instance = new DatasetValues32(dataset, $http, $rootScope);
        return {        	
        	getByKey: instance.getByKey.bind(instance),  
        	getSome: instance.getSome.bind(instance),
        	getDict: instance.getDict.bind(instance)
        };
    };
});