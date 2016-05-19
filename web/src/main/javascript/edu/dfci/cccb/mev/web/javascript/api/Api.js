define ([ 'angular', 'lodash', 'angular-resource', './AnalysisEventBus', '../dataset/lib/AnalysisClass'], function (angular, _, angularResource, AnalysisEventBus, AnalysisClass) {
	
    return angular
    .module ('Mev.Api', ['ngResource'])
    .service('AnalysisEventBus', AnalysisEventBus)
    .service ('DatasetResourceService', ['$resource', '$q', '$http', '$rootScope',
                                             function ($resource, $q, $http, $rootScope) {
    	 
    	var resource = $resource('/dataset/:datasetName/data',{format: "json"},
            {
                'get': {method:'GET'},
                'getAll': {
                    url: '/dataset',
                    method: 'GET',
                    isArray: true
                },
                'subset': {
                    url: '/dataset/:datasetName/data/subset/export',
                    method: "POST"
                }
            });       	 
    	var DatasetResource = Object.create(resource);       	 
    	
    	DatasetResource.get = function(params, data, callback){
    		var dataset = resource.get(params, data, callback);    		
    		return dataset;
    	 };
    	 DatasetResource.getAll = function(params, data, callback){
            var datasetsResource = resource.getAll(params, data, callback);
            datasetsResource.$promise.then(function(response){
                $rootScope.$broadcast("mev:datasets:list:refreshed", response);
            });
            return datasetsResource;
         };
         DatasetResource.subset = function(params, data, callback){        
            data.name = params.datasetName + "--" + data.name;    
            var datasetsResource = resource.subset(params, data, callback);            
            datasetsResource.$promise.then(function(response){
                $http({
                   method:"POST", 
                   url:"/annotations/" + params.datasetName + "/annotation/row" 
                   + "/export?destId="+data.name});
               $http({
                   method:"POST", 
                   url:"/annotations/" + params.datasetName + "/annotation/column" 
                   + "/export?destId="+data.name});
               DatasetResource.getAll();
            })
            return datasetsResource;
         };

    	 return DatasetResource;
    }])
    .service('GoogleDriveResourceService', ['$resource', function($resource){
        	return $resource('/import/google',
	    	{
	    		'format':'json'
			},{
				'get': {method:'GET'},
				'post':{
					url:'/import/google/:id/load',
					method: 'POST'
				}
			});
        }])
    .service ('AnalysisResourceService', ['$resource', '$http','$routeParams', "$timeout", 'AnalysisEventBus', 
                                          function ($resource, $http, $routeParams, $timeout, analysisEventBus) {
    		
    	var transformRequest = [function(data, headers){
            console.log("transformRequest", data);
            return data;
    	}].concat($http.defaults.transformRequest);
    	
    	var resource = $resource('/dataset/:datasetName/analysis',
	    	{	'format':'json'}, 
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
    	AnalysisResource.post=postWrapper("post");
    	AnalysisResource.postf=postWrapper("postf");
    	AnalysisResource.post3=postWrapper("post3");
    	AnalysisResource.put=postWrapper("put");

    	function postWrapper(methodName){
    		return function(params, data, callback){
                if(params.analysisName && params.analysisName.toLowerCase() === params.analysisType.toLowerCase()){
                    //do not prefix analysis name with type - name already contains the type
                }else{
                    if(params.analysisName)
                        params.analysisName = params.analysisType + "_" + params.analysisName;
                    if(data.name)
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
    					angular.extend(allParams, params);    					
    					
    					angular.extend(allParams, data);
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
    								angular.extend(analysis.params, allParams);
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
    	        		};
    					
//    					analysisEventBus.analysisStarted(response);
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
    	
    }])
    .service ('SelectionResourceService', ['$resource', '$routeParams', '$http', 'DatasetResourceService', '$rootScope',
    function($resource, $routeParams, $http, datasetResource, $rootScope){
    	
    	var resource = $resource('/dataset/:datasetName/:dimension/selection',{
    		'format': 'json'
    	}, {
    		'getAll': {
    		    'url':'/dataset/:datasetName/:dimension/selections', 
    		    'method':'GET'},
    		'get': {
    			'method': 'GET',
    			'url': '/dataset/:datasetName/:dimension/selection/:selectionName'
    		},
    		'post':{
    			'method': 'POST',
    			'url':"/dataset/:datasetName/:dimension/selection/",
    		},
			'put':{
				'method': 'PUT',
				'url':"/dataset/:datasetName/:dimension/selection/",
			},
    		'export':{
    			'method': 'POST',
    			'url':"/dataset/:datasetName/:dimension/selection/export",
    		},
            'delete': {
                'method': 'DELETE',
                'url': '/dataset/:datasetName/:dimension/selection/:selectionName'
            }
    		
    	});
    	
//    	return resource;
    	var SelectionResource = Object.create(resource);    	
        SelectionResource.post=function(params, data, callback){
            var result = resource.post(params, data, callback);     
            result.$promise.then(function(response){
                $rootScope.$broadcast("mui:dataset:selections:added", params.dimension, params, data, response);
            });
            return result;
        };
		SelectionResource.put=function(params, data, callback){
			var result = resource.put(params, data, callback);
			result.$promise.then(function(response){
				$rootScope.$broadcast("mui:dataset:selections:added", params.dimension, params, data, response);
			});
			return result;
		};
    	SelectionResource.getAll=function(params, data, callback){
    		var result = resource.getAll(params, data, callback);
    		result.$promise.then(function(response){
    			response.selections.map(function(selection){
    				selection.type=params.dimension;
    			});    			
    		});
    		return result;
    	};
        SelectionResource.export=function(params, data, callback){
            data.name = params.datasetName + "--" + data.name;
            var result = resource.export(params, data, callback);            
            result.$promise.then(function(response){
                $http({
                   method:"POST", 
                   url:"/annotations/" + params.datasetName + "/annotation/row" 
                   + "/export?destId="+data.name});
               $http({
                   method:"POST", 
                   url:"/annotations/" + params.datasetName + "/annotation/column" 
                   + "/export?destId="+data.name});
               datasetResource.getAll();
            })
        }
    	
    	return SelectionResource;
    }]);
    
    	
})
