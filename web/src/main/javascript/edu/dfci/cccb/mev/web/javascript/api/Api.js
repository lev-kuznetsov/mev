define ([ 'angular', 'angularResource'], function (angular) {

    return angular
    .module ('Mev.Api', ['ngResource'])
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
					url:'import/google/:id/load',
					method: 'POST'
				}
			});
        }])
    .service ('AnalysisResourceService', ['$resource', '$routeParams', function ($resource, $routeParams) {
    	
    	return $resource('/dataset/:datasetName/analysis',
	    	{
	    		'format':'json'
			}, {
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
                	'url': 'dataset/:datasetName'
                        + '/analyze/:analysisType/:analysisName(:analysisParams)'
                },
                'post': {
                    'method':'POST',
                    'url': 'dataset/:datasetName'
                        + '/analyze/:analysisType'
                },
                'post3': {
                	'method':'POST',
                	'url': 'dataset/:datasetName'+
                		'/analyze/:analysisType/:analysisName',
                		//'headers':{'Content-Type':'application/x-www-form-urlencoded'}
                }
                
			});
    	
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