"use strict";
define(["mui", "../../events/AnalysisEventBus", "mev-dataset/src/main/dataset/lib/AnalysisClass"], function(ng, AnalysisEventBus, AnalysisClass){
    function AnalysisRest($resource, $http, $timeout, analysisEventBus) {
            
        var transformRequest = [function(data, headers){
            console.log("transformRequest", data);
            return data;
        }].concat($http.defaults.transformRequest);
        
        var resource = $resource('/dataset/:datasetName/analysis',
            {   'format':'json'}, 
            {
                'getAll': {
                    'url' : '/dataset/:datasetName/analysis',
                    'method':"GET",                 
                },
                'get': {
                    'url': '/dataset/:datasetName'
                    + '/analysis/:analysisName', 
                    'method':"GET"
                },
                'postf': {
                    'method':'POST',
                    'url': '/dataset/:datasetName'
                        + '/analyze/:analysisType/:analysisName(:analysisParams)'
                },
                'post': {
                    'method':'POST',
                    'url': '/dataset/:datasetName'
                        + '/analyze/:analysisType'
                },
                'post3': {
                    'method':'POST',
                    'url': '/dataset/:datasetName'+
                        '/analyze/:analysisType/:analysisName',
                        //'headers':{'Content-Type':'application/x-www-form-urlencoded'}
                },
                'post4': {
                    'method':'GET',
                    'url': '/dataset/:datasetName'+
                        '/analyze/:analysisType/:analysisName',
                        //'headers':{'Content-Type':'application/x-www-form-urlencoded'}
                },
                'put': {
                    'method': 'PUT',
                    'url': '/dataset/:datasetName'+
                        '/analyze/:analysisType/:analysisName'
                }
                
            });             

        var AnalysisResource = Object.create(resource);
        AnalysisResource.post=postWrapper("post");
        AnalysisResource.postf=postWrapper("postf");
        AnalysisResource.post3=postWrapper("post3");
        AnalysisResource.put=postWrapper("put");

        function postWrapper(methodName){
            return function(params, data, callback){
                
                var result = resource[methodName](params, data, callback);
                
                result.$promise.then(
                    function(response){
                        
                        if(typeof data === "string")
                            data = JSON.parse(data);
                        if(Array.isArray(data))
                            data = {data: data};
                        var allParams = {
                            analysisName: params.analysisName || data.analysisName || params.name || data.name || response.name
                        };
                        ng.extend(allParams, params);                      
                        
                        ng.extend(allParams, data);
                        console.debug("AnalysisResource success", params, "data", data, "response", response);
                        var sessionStorageKey = allParams.datasetName+"."+allParams.analysisName;
                        console.debug("sessionStorageKey set", sessionStorageKey);
                        sessionStorage.setItem(sessionStorageKey, JSON.stringify(allParams));  
                        
                        function poll(prevResponse, wait){
                            if(prevResponse.status && prevResponse.status === "IN_PROGRESS"){                               
                                $timeout(function(){
                                    var pollParams = {datasetName: allParams.datasetName, analysisName: allParams.analysisName};
                                    AnalysisResource.get(pollParams,
                                        function(newResponse){      
                                            poll(newResponse, 5000);
                                        });                             
                                }, wait);           
                            }else{
                                var analysis = new AnalysisClass(prevResponse);                            
                                if(analysis.params)                                 
                                    ng.extend(analysis.params, allParams);
                                else
                                    analysis.params = allParams;
                                if(prevResponse.status === "ERROR"){                                    
                                    console.error("PollAnalysis error", analysis.name, analysis);                               
                                    analysisEventBus.analysisFailed(params, data, analysis);    
                                }else{
                                    console.log("PollAnalysis result", analysis.name, analysis);                               
                                    analysisEventBus.analysisSucceeded(params, data, analysis);    
                                }
                                
                            }
                        }
                        
//                      analysisEventBus.analysisStarted(response);
                        analysisEventBus.analysisStarted(allParams.analysisType, allParams.analysisName, new AnalysisClass(response));
                        poll(response, 500);
                        
                                            
                    }, function(response){
                        console.debug("AnalysisResource error", response);
                        analysisEventBus.analysisFailed(params, data);
                    }
                );
                
                return result;
            };
        }
        
        
        return AnalysisResource;        
        
    }
    AnalysisRest.$inject=["$resource", "$http", "$timeout", "mevAnalysisEventBus"];
    AnalysisRest.$name="mevAnalysisRest";
    AnalysisRest.$provider="service";
    return AnalysisRest;

});