define ([ 'angular', 'angularResource', './AnalysisEventBus'], function (angular, angularResource, AnalysisEventBus) {

    return angular
    .module ('Mev.Api', ['ngResource'])
    .service('AnalysisEventBus', AnalysisEventBus)
    .service ('DatasetResourceService', ['$resource', function ($resource) {
    	 return $resource('/dataset/:datasetName/data',
	    	{
	    		'format':'json'
			},{
				'get': {method:'GET'}
			});
    	 
    	
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
    .service ('AnalysisResourceService', ['$resource', '$routeParams', 'AnalysisEventBus', function ($resource, $routeParams, analysisEventBus) {
    	    	
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
                }
                
			});    	    	
    	function postWrapper(methodName){
    		return function(params, data, callback){
        		
        		var result = resource[methodName](params, data, callback);
        		
        		result.$promise.then(
    				function(response){
    					console.debug("AnalysisResource success", params, response, data);
    					analysisEventBus.analysisSucceeded(params, data);
    	    		}, function(response){
    	    			console.debug("AnalysisResource error", response);
    	    			analysisEventBus.analysisFailed(params, data);
    	    		}
    	    	);
        		
        		analysisEventBus.analysisStarted(params, data);
    		};
    	}
    	var AnalysisResource = Object.create(resource);
    	AnalysisResource.post=postWrapper("post");
    	AnalysisResource.postf=postWrapper("postf");
    	
    	
    	return AnalysisResource;    	
    	
    }])
    .service ('SelectionResourceService', ['$resource', '$routeParams', function($resource, $routeParams){
    	return $resource('/dataset/:datasetName/:dimension/selection',{
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
    		
    	})
    }]);
    
    	
})