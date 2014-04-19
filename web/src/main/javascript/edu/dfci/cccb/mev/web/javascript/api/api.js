define ([ 'angular'], function (angular) {

    angular
    .module ('Mev.api', [])
    .service ('api.dataset', ['$resource', '$routeParams', function ($resource, $routeParams) {
    	 return $resource('/dataset/'+$routeParams.datasetName+'/data',
	    			{
	    		format:'json'
			},{
				get: {method:'GET'}
			});
    	 
    	
    }])
    .service ('api.dataset.analysis', ['$resource', '$routeParams', function ($resource, $routeParams) {
    	
    	return $resource('/dataset/' + $routeParams.datasetName + '/analysis',
	    	{
	    		format:'json'
			}, {
				getAll: {method:"GET"},
				get: {
					url: '/dataset/'
                    + $routeParams.datasetName
                    + '/analysis/:analysisName', 
                    method:"GET"
                },
                post: {
                	method:'POST',
                	url: 'dataset/'
                        + $routeParams.datasetName
                        + '/analyze/:analysisType'
                }
			});
    	
    }])
    .service ('api.dataset.selections', ['$resource', '$routeParams', function($resource, $routeParams){
    	return $resource('/dataset/'+ $routeParams.datasetName +'/:dimension/selection',{
    		format: 'json'
    	}, {
    		getAll: {method:'GET'},
    		get: {
    			method: 'GET',
    			url: '/dataset/'+ $routeParams.datasetName +'/:dimension/selection/:selectionName'
    		},
    		post:{
    			method: 'POST',
    			url:"/dataset/" + $routeParams.datasetName + "/:dimension"
    	        + "/selection/",
    		}
    		
    	})
    }]);
    
    	
})