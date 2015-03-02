define(["./Dashboard", "../annotations/AnnotationSet", "../annotations/AnnotationSetRepository"], 
function(Dashboard, AnnotationSet, AnnotationSetRepository){
	var DashboardRepository = function($q, $timeout, $http, AnnotationSetRepository){
		//private
		var dashboards = [];		
		var self=this;
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
		
		//public		
		self.create=function(dashboardName){
			return AnnotationSetRepository.get(dashboardName).then(function(annotationSet){
				console.debug("Dashbaord.create->annotationSet", dashboardName, annotationSet );
				var dashboard=new Dashboard({"annotationSet": annotationSet, "facets": [1, 2, 3]}); 
				self.put(dashboard);
				return dashboard;
			});
		};
//		self.create = $timeout(create, 1000);
				
		self.get=function(name){
			var dashboard;
			Arrays.some(dashboards, function(cur){
				if(cur.name===name){
					console.debug("found dashboard", cur);
					dashboard=cur;
					return true;
				}
			});
			return makeapromise(dashbaord);
		};
		self.exists=function(name){
			try{
				console.debug("exists?", name);
				for(var i=0;i<dashboards.length;i++){
					console.debug("cur dashbaord", dashboards[i]);
					if(dashboards[i].getName()===name){
						console.debug("exists?true", name);
						return makeapromise(dashboards[i]);					
					}
				}
				console.debug("exists?false", name);
				return breakapromise(name);
			}catch(e){
				console.error("Unbale to check existance of "+name,"; error:",e)
				return breakapromise(e.message)
			}
		};
		self.getAll=function(){
//			console.debug("Object.keys(dashboards)", Object.keys(dashboards));
//			 return Object.keys(dashboards).map(function (key) {
//				    return dashboards[key];
//				});			
//			return makeapromise(dashboards);
			return $http.get("api/dashboard").success(function(data, status, headers, config){
				dashboards.splice(0, dashboards.length)
				data.map(function(dashboard){
					dashboards.push(new Dashboard(dashboard));
				});				
			}).then(function(){ 
				return dashboards;
			});
		};
		
		self.put=function(dashboard){
			console.debug("put dashboard", dashboard);
//			console.debug("dashboard.constructor.prototype", dashboard.constructor.prototype);
//			console.debug("AnnotationSet.prototype", AnnotationSet.prototype);
//			console.debug("instanceof dashboard", dashboard instanceof AnnotationSet);
//			if(dashboard instanceof AnnotationSet){
//				self.create(dashboard);
//			}else{
			
//				dashboards[dashboard.name]=dashboard;
//			dashboards.push(dashboard);
			$http.put("api/dashboard/"+dashboard.getName(), dashboard.toJson()).success(function(data, status, headers, config){
				dashboards.push(new Dashboard(data));
			});
//			}	
		};		
	};
	return DashboardRepository;
});