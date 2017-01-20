define(["mui", "lodash", "../../events/AnalysisEventBus", "mev-dataset/src/main/dataset/lib/AnalysisClass"], function(ng, lodash, AnalysisEventBus, AnalysisClass){
    function AnalysisRest($resource, $http, $timeout, $q, analysisEventBus, mevDb, mevWorkspace) { "use strict";
            
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
                },
                'delete': {
                    'method': 'DELETE',
                    'url': '/dataset/:datasetName/analysis/:analysisName'
                }
                
            });             

        var AnalysisResource = Object.create(resource);
        AnalysisResource.getAll=function(params, data, callback){

            var deferred = $q.defer();
            var cache = [];
            cache.$promise = deferred.promise;
            cache.$resolved = false;

            mevWorkspace.getDataset(params.datasetName)
                .then(function(dataset){
                    if(dataset && dataset.isActive){
                        return resource.getAll(params, data, callback).$promise
                    }else{
                        return {
                            names: []
                        };
                    }
                })
                .then(function(remote){
                    return mevDb.getAnalyses(params.datasetName)
                        .then(function(local){
                            var all = {
                                names: _.union(remote.names, local)
                            };
                            deferred.resolve(all);
                            return all;
                        });
                })
                .catch(function(e){
                    console.error("Error fetching dataset list: ", params, e);
                    deferred.reject(e);
                    throw new Error("Error fetching dataset list: " + JSON.stringify(e));
                });

            return cache;
        };
        AnalysisResource.delete=function(params, data, callback){

            var deferred = $q.defer();
            var cache = [];
            cache.$promise = deferred.promise;
            cache.$resolved = false;
            mevWorkspace.getDataset(params.datasetName)
                .then(function(dataset){
                    if(dataset && dataset.isActive){
                        return resource.getAll(params, data, callback).$promise
                            .then(function(remoteResponse){
                                if(_.includes(remoteResponse.names, params.analysisName)){
                                    return resource.delete(params).$promise
                                        .then(function(deleteResponse){
                                            console.debug("deleteResponse", deleteResponse);
                                            deleteResponse.status = 200;
                                            return deleteResponse;
                                        })
                                        .catch(function(e){
                                            if(e.status === 404)
                                                return e;
                                            else
                                                throw e;
                                        });
                                }else{
                                    return {
                                        status: 404
                                    }
                                }
                            })
                    }else{
                        return {
                            status: 200
                        };
                    }
                })
                .then(function(remote){
                    if(remote && (remote.status === 200 || remote.status === 404))
                        return mevDb.deleteAnalysis(params.datasetName, params.analysisName);
                    return remote;
                })
                .then(function(deleteResult){
                    deferred.resolve(deleteResult);
                })
                .catch(function(e){
                    console.error("Error deleting analysis: ", params, e);
                    deferred.reject(e);
                    throw new Error("Error deleting analysis : " + JSON.stringify(e));
                });

            return cache;
        };

        AnalysisResource.get = function(params, callback){
            var deferred = $q.defer();
            var cache = {
                $promise: deferred.promise,
                $resolve: false
            };

            mevDb.getAnalysis(params.datasetName, params.analysisName)
                .catch(function(e){
                    if(e.status===404)
                        return resource.get(params).$promise
                            .then(function(response){
                                if(response.status!=="IN_PROGRESS")
                                    mevDb.putAnalysis(params.datasetName, response);
                                return response;
                            });
                    else
                        throw new Error("Error retrieving analysis from cache: " + JSON.stringify(params));
                })
                .then(function(doc){
                    if(callback)
                        callback(doc);
                    deferred.resolve(doc);
                })
                .catch(function(e){
                    console.log("Error getting analysis: ", e);
                    deferred.reject(e);
                    throw new Error("Error getting analysis: " + JSON.stringify(e));
                });

            return cache;
        };
        AnalysisResource.post=postWrapper("post");
        AnalysisResource.postf=postWrapper("postf");
        AnalysisResource.post3=postWrapper("post3");
        AnalysisResource.put=postWrapper("put");
        
        function postWrapper(methodName){
            return function(params, data, callback){
                        if (params.analysisName && params.analysisName.toLowerCase().indexOf(params.analysisType.toLowerCase()) > -1) {
                            //do not prefix analysis name with type - name already contains the type
                        } else if (data.name && data.name.toLowerCase().indexOf(params.analysisType.toLowerCase()) > -1) {
                            //do not prefix analysis name with type - name already contains the type
                        } else {
                            if (params.analysisName)
                                params.analysisName = params.analysisType + "_" + params.analysisName;
                            if (data.name)
                                data.name = params.analysisType + "_" + data.name;

                        }
                
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
                        _.assign(allParams, data);
                        _.assign(allParams, params);
                        console.debug("AnalysisResource success", params, "data", data, "response", response);
                        var sessionStorageKey = allParams.datasetName+"."+allParams.analysisName;
                        console.debug("sessionStorageKey set", sessionStorageKey);
                        delete params.limma;
                        delete allParams.limma;
                        sessionStorage.setItem(sessionStorageKey, JSON.stringify(allParams));  
                        
                        function poll(prevResponse, wait){
                            if(prevResponse.status && prevResponse.status === "IN_PROGRESS"){                               
                                $timeout(function(){
                                            var pollParams = {
                                                datasetName: allParams.datasetName,
                                                analysisName: allParams.analysisName
                                            };
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
                                mevDb.putAnalysis(allParams.datasetName, analysis);
                                if(prevResponse.status === "ERROR"){                                    
                                    console.error("PollAnalysis error", analysis.name, analysis);                               
                                    analysisEventBus.analysisFailed(params, data, analysis);    
                                }else{
                                    console.log("PollAnalysis result", analysis.name, analysis);                               
                                    analysisEventBus.analysisSucceeded(params, data, analysis);    
                                }
                                
                            }
                        };
                        
//                      analysisEventBus.analysisStarted(response);
                        analysisEventBus.analysisStarted(allParams.analysisType, allParams, new AnalysisClass(response));
                        poll(response, 500);
                        
                                            
                    }, function(response){
                        console.debug("AnalysisResource error", response);
                        analysisEventBus.analysisFailed(params, data, response);
                    }
                );
                
                return result;
            };
        }
        
        
        return AnalysisResource;        
        
    }
    AnalysisRest.$inject=["$resource", "$http", "$timeout", "$q", "mevAnalysisEventBus", "mevDb", "mevWorkspace"];
    AnalysisRest.$name="mevAnalysisRest";
    AnalysisRest.$provider="service";
    return AnalysisRest;

});