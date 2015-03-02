define(["ng", "lodash"], function(ng, _){
	return function DatasourceResourceRepository($resource){
		var root="api";
		var listUri=root+"/datasource";
		var entityUri=listUri+"/:datasourceId"
		var data=[];
		var source = $resource(entityUri, {datasourceId: "@id"},				
				{
					getAll: {
						url: listUri,
						method: "GET",
						isArray: true
					},
					put: {
						url: entityUri,
						method: "PUT"
					}
				});
		
		source.prototype.$update=function(){
			updateSource=this;
			return source.put(updateSource
					, function(response){
				_.map(data, function(cur){
					if(cur.id===response.id)
						if(cur!==updateSource)
							_.extend(cur, response);
				});
			}
			);
		}
		function _getAll(response){
			while(data.length>0){
				data.pop();
			}
			_.map(response, function(item){
				data.push(item);
			})
			console.log("getaAll success", data);
		}
		
		return {
			getAll: function(){
				var all = source.getAll(_getAll, function(response){
					console.error("getAll failed for " + listUri, response);
				});
				data.$promise=all.$promise;
				return data;
			},
			get: function(id){
				var exists = _.filter(data, function(item){
					return item.id==id;
				});
				if(exists.length>0)
					return exists[0];
				
				return source.get({datasourceId: id});
			},
			put: function(item){	
					if(!item.id){
						var newItem = source.save(item);
						data.push(newItem);
						return newItem;
					}else{						
						return source.put(item, function(response){
							_.map(data, function(cur){
								if(cur.id===item.id)
									_.extend(cur, response);
							});
						});
					}
					
			}
		}
	};
});