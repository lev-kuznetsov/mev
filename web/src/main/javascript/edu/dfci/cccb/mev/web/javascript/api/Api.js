define ([ 'angular'], function (angular) {

    angular
    .module ('Mev.Api', [])
    .service ('DatasetResourceService', ['$resource', function ($resource) {
    	 return $resource('/dataset/:datasetName/data',
	    	{
	    		'format':'json'
			},{
				'get': {method:'GET'}
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
                'post': {
                	'method':'POST',
                	'url': 'dataset/:datasetName'
                        + '/analyze/:analysisType/:analysisName(:analysisParams)'
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
    		}
    		
    	})
    }]);
    
    	
})