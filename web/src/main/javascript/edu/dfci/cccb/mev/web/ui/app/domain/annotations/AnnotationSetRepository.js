define(["ng", "./AnnotationSet"], function(ng, AnnotationSet){
	 return function($q, $timeout, $http){
		var that = {};
		
		//private
		var makeapromise=function(value){
			var deferred = $q.defer();
	        deferred.resolve(value);
	        return deferred.promise;
		};
		var breakapromise=function(value){
			var deferred = $q.defer();
	        deferred.reject("dashboard not found:"+value);
	        return deferred.promise;
		};
		
		
		//private
		var initPromise;
		var annotations=[];
		function _init(){	
//			initPromise=$timeout(function(){
//				console.debug("timoute!!!");
//				for(var i=0;i<10;i++){
//					annotations.push(new AnnotationSet(
//							{meta: {
//									name: "name"+i,
//									description: "just a '"+i+"'",
//									numberOfSamples: i*10
//								},
//								data: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
//							}
//						));
//				}
//				console.debug("AnnotationSetRepository.init", annotations.length);
//			}, 1000);
			initPromise=$http.get("api/annotations").success(function(data, status, headers, config){
				console.debug("$http completed", data, status, headers, config);
				annotations=data;
			});
		}
		
		//public
		that.getAll=function(){			
			return initPromise.then(function(){
				return annotations;
			});
		};		
		that.get=function(name){
			return initPromise.then(function(){
				var results = annotations.filter(function(ann) {
					  return ann.meta.name === name;
				});			
				return results? makeapromise(results[0]) : breakapromise(name); // or null
			});
		};
		
		//construct		
//		_init();		
		return that;
	 };
});