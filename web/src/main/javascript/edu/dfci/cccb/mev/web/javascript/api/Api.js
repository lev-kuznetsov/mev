define ([ 'angular', 'lodash', 'angularResource', './AnalysisEventBus'], function (angular, _, angularResource, AnalysisEventBus) {
	
    return angular
    .module ('Mev.Api', ['ngResource'])
    .service('AnalysisEventBus', AnalysisEventBus)
    .service ('DatasetResourceService', ['$resource', '$q', '$http', 
                                             function ($resource, $q, $http, DatasetValuesResource) {
    	 
    	var resource = $resource('/dataset/:datasetName/data',{format: "json"},{'get': {method:'GET'}});       	 
    	var DatasetResource = Object.create(resource);       	 
    	
    	DatasetResource.get = function(params, data, callback){
    	
    		var dataset = resource.get(params, data, callback);
    		var valuesPromise;
    		return dataset.$promise.then(function(datasetRespObj){
    			valuesPromise = $http.get('/dataset/'+params.datasetName+'/data/values', {params: {format: "binary"}, responseType: "arraybuffer", headers: {"Accept": "application/octet-stream"}});
    			return valuesPromise;
    		}).then(function(values) {
 	        	var ab = values.data;     				
 				var dataview = new DataView(ab);
 				console.debug("swap: array", ab.byteLength);				   	      				
 				dataset.valuesBuffer = ab;
 				dataset.dataview = dataview;
 				return dataset;
// 				return dataset;
			})["catch"](function(e){
				throw e;
			});    		    		
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
    .service ('AnalysisResourceService', ['$resource', '$http','$routeParams', 'AnalysisEventBus', function ($resource, $http, $routeParams, analysisEventBus) {
    		
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
                }
                
			});    	    	
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
    					angular.extend(allParams, params);    					
    					
    					angular.extend(allParams, data);
    					console.debug("AnalysisResource success", params, "data", data, "response", response);
    					var sessionStorageKey = allParams.datasetName+"."+allParams.analysisName;
    					console.debug("sessionStorageKey set", sessionStorageKey);
    					sessionStorage.setItem(sessionStorageKey, JSON.stringify(allParams));    	    			
    					analysisEventBus.analysisSucceeded(params, data);
    	    		}, function(response){
    	    			console.debug("AnalysisResource error", response);
    	    			analysisEventBus.analysisFailed(params, data);
    	    		}
    	    	);
        		
        		analysisEventBus.analysisStarted(params, data);
        		return result;
    		};
    	}
    	var AnalysisResource = Object.create(resource);
    	AnalysisResource.post=postWrapper("post");
    	AnalysisResource.postf=postWrapper("postf");
    	AnalysisResource.post3=postWrapper("post3");
    	AnalysisResource.put=postWrapper("put");
    	
    	
    	return AnalysisResource;    	$promise
    	
    }])
    .service ('SelectionResourceService', ['$resource', '$routeParams', function($resource, $routeParams){
    	
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
    		'export':{
    			'method': 'POST',
    			'url':"/dataset/:datasetName/:dimension/selection/export",
    		}
    		
    	});
    	
//    	return resource;
    	var SelectionResource = Object.create(resource);
    	SelectionResource.getAll=function(params, data, callback){
    		var result = resource.getAll(params, data, callback);
    		result.$promise.then(function(response){
    			response.selections.map(function(selection){
    				selection.type=params.dimension;
    			});
    		});
    		return result;
    	};
    	
    	return SelectionResource;
    }]);
    
    	
})
