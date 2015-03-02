define(["lodash"], function(_){
	
	function DataRepositoryMixin(spec){
		
		var $http=spec.$http;
		var url=spec.url;
		var fnGetId=initFnGetId(spec.getId);
		var fnSetId=initFnSetId(spec.setId);
		function initFnGetId(fn){
			if(_.isFunction(spec.getId)){
				return fn;				
			}else if(typeof fn === "string"){
				return function(){return this[fn];}
			}else{
				return function(){return this.name;};
			}
		}
		function initFnSetId(fn){
			if(_.isFunction(fn)){
				return fn;
			}else if(typeof fn === "string"){
				return function(id){this[fn]=id;}
			}else{
				return function(id){this.name=id;};
			}
		}
		
		this._nextId=0;
		this._generateNextId=function(){
			return this._nextId++;
		};
		
		this.get=function(id){
			return $http.get(url+"/"+id).error(function(data){
				console.error("$http.get error", data);
			}).success(function(data, status, headers, config){
				_.remove(this.data, function(item){
					return fnGetId.call(item)===fnGetId.call(data);
				});
				this.data.push(data);
				return data;
			}.bind(this))
			.then(function(response){
				return response.data;
			});
		};
		
		this.put=function(obj){
			if(!fnGetId.call(obj)){
//				fnSetId.call(obj, this._generateNextId());				
				return $http.post(url, obj).error(function(data){
					console.error("$http.put error", data);
				}).success(function(data, status, headers, config){
//					obj.name="Undefined "+ this._generateNextId();
					this.data.push(data);
					return data;
				}.bind(this)
				).then(function(response){
					return response.data;
				});
			}else{
				return $http.put(url+"/"+fnGetId.call(obj), obj).error(function(data){
					console.error("$http.put error", data);
				}).success(function(data, status, headers, config){
					_.remove(this.data, function(item){
						return fnGetId.call(item)===fnGetId.call(data);
					});
					this.data.push(data);
					return data;
				}.bind(this)
				).then(function(response){
					return response.data;
				});
			}
		};		
		
		this.getAll=function(){
			return $http.get(url).error(function(data){
				console.error("$http.getAll error", data);
			}).success(function(data, status, headers, config){
				while(this.data.length>0){
					this.data.pop();
				}
				data.map(function(item){
					this.data.push(item);
				}.bind(this));				
			}.bind(this))
			.then(function(response){
				return this.data;
			}.bind(this));
		};
		
	};
//	console.debug("ServerStorageMixin init", ServerStorageMixin);
		
	return DataRepositoryMixin;	
});