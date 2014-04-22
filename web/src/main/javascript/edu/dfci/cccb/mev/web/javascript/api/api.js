define ([ 'angular'], function (angular) {

    angular
    .module ('Mev.api', [])
    .service ('api.dataset', ['$resource', function ($resource) {
    	 return $resource('/dataset/:datasetName/data',
	    	{
	    		format:'json'
			},{
				get: {method:'GET'}
			});
    	 
    	
    }])
    .service ('api.dataset.analysis', ['$resource', '$routeParams', function ($resource, $routeParams) {
    	
    	return $resource('/dataset/:datasetName/analysis',
	    	{
	    		format:'json'
			}, {
				getAll: {method:"GET"},
				get: {
					url: '/dataset/:datasetName'
                    + '/analysis/:analysisName', 
                    method:"GET"
                },
                post: {
                	method:'POST',
                	url: 'dataset/:datasetName'
                        + '/analyze/:analysisType'
                }
			});
    	
    }])
    .service ('api.dataset.selections', ['$resource', '$routeParams', function($resource, $routeParams){
    	return $resource('/dataset/:datasetName/:dimension/selection',{
    		format: 'json'
    	}, {
    		getAll: {method:'GET'},
    		get: {
    			method: 'GET',
    			url: '/dataset/:datasetName/:dimension/selection/:selectionName'
    		},
    		post:{
    			method: 'POST',
    			url:"/dataset/:datasetName/:dimension"
    	        + "/selection/",
    		}
    		
    	})
    }]);
    
    	
})