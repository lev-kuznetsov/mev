define ([ 'angular'], function (angular) {

    angular
    .module ('Mev.api', [])
    .service ('api.dataset', ['$resource', '$routeParams', function ($resource, $routeParams) {
    	
    	return $resource('/dataset/'+$routeParams.datasetName+'/data',
    			{
    		format:'json'
		});
    	
    }])
    .service ('api.dataset.analysis.list', ['$resource', '$routeParams', function ($resource, $routeParams) {
    	
    	return $resource('/dataset/' + $routeParams.datasetName + '/analysis',
	    	{
	    		format:'json'
			}, {
				get:{method:"GET", isArray:true} 
			});
    	
    }]);
    	
})