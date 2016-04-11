define(["lodash"], function(_){

	function sleep(milliseconds) {
		var start = new Date().getTime();
			for (var i = 0; i < 1e7; i++) {
			if ((new Date().getTime() - start) > milliseconds){
			   break;
			}
		}
	};
	
	function walkObjectTree(obj, callback, path, limit){
		var path = path || [];

		_.transform(obj, function(acc, value, key, obj){
			if(_.isObject(value)){
				acc.push(key);
				console.debug("push:", acc.join("."));
				callback(value, acc);
				walkObjectTree(value, callback, acc);
				console.debug("pop:", acc.join("."));
				path.pop();	
				
			}else if(_.isArray(value)){
				acc.push(key);
				console.debug("push:", acc.join("."));				
				_.forEach(value, function(item){
						callback(item, acc)					
						walkObjectTree(item, callback, acc);
						console.debug("pop:", acc.join("."));
						path.pop();	
						
				});								
				acc.pop();
				console.debug("pop:", acc.join("."));
			}else if(!_.isFunction(value) && !_.isUndefined(value) && !_.isEmpty(value) && !_.isNull(value)){
				acc.push(key);
				console.debug("push:", acc.join("."));
				callback(value, acc);					
				walkObjectTree(value, callback, acc);
				console.debug("pop:", acc.join("."));
				path.pop();	
				
			}								
		}, path);
		
	};

	function walkObjectTree2(obj, callback, path, limit){
		var path = path || [];

		_.transform(obj, function(acc, value, key, obj){
			if(_.isObject(value)){
				acc.push(key);
				console.debug("push2:", acc.join("."));
				if(callback(value, acc))
					walkObjectTree2(value, callback, acc);
				console.debug("pop2:", acc.join("."));
				path.pop();	
				
			}else if(_.isArray(value)){
				acc.push(key);
				console.debug("push2:", acc.join("."));
				if(callback(value, acc)){
					_.forEach(value, function(item){				
						acc.push(item, acc);
						console.debug("push2:", acc.join("."));
						var keepGoing=callback(item, acc)
						if(keepGoing){			
							walkObjectTree2(item, callback, acc);
							console.debug("pop2:", acc.join("."));
							acc.pop();
						}else{
							console.debug("aborting ... pop2:", acc.join("."));
							acc.pop();
							return false;
						}
							
					});						
				}
				acc.pop();
				console.debug("pop2:", acc.join("."));
			}else if(!_.isFunction(value) && !_.isUndefined(value) && !_.isEmpty(value) && !_.isNull(value)){
				acc.push(key);
				console.debug("push2:", acc.join("."));
				if(callback(value, acc))
					walkObjectTree2(value, callback, acc);
				console.debug("pop2:", acc.join("."));
				path.pop();	
				
			}								
		}, path);
		
	};
	
	function objectToNodeTree(obj, callback, path, tree, limit){
		var path = path || [];
		var tree=tree || {nodeName: "root", nodes: []};		
		var newNode;
		_.transform(obj, function(acc, value, key, obj){
			if(_.isObject(value)){
				acc.push({key: key, node: tree});				
//				console.debug("push2:", acc.join("."));
				newNode=callback(value, acc)				
				if(newNode){
					tree=newNode;										
					objectToNodeTree(value, callback, acc, tree);					
				}
//				console.debug("pop2:", acc.join("."));
				tree=acc.pop().node;	
				
			}else if(_.isArray(value)){
				acc.push({key: key, node: tree});
//				console.debug("push2:", acc.join("."));
				newNode=callback(value, acc)				
				if(newNode){
					tree=newNode;
					_.forEach(value, function(item){				
						acc.push({key: item, node: tree});
//						console.debug("push2:", acc.join("."));
						newNode=callback(value, acc)				
						if(newNode){
							tree=newNode;
							objectToNodeTree(item, callback, acc, tree);
//							console.debug("pop2:", acc.join("."));
							tree=acc.pop().node;
						}else{
//							console.debug("aborting ... pop2:", acc.join("."));
							tree=acc.pop().node;
							return false;
						}
					});						
				}
				tree=acc.pop().node;
//				console.debug("pop2:", acc.join("."));
			}else if(!_.isFunction(value) && !_.isUndefined(value) && !_.isEmpty(value) && !_.isNull(value)){
				acc.push({key: key, node: tree});
//				console.debug("push2:", acc.join("."));
				newNode=callback(value, acc)				
				if(newNode){
					tree=newNode;
					objectToNodeTree(value, callback, acc, tree);
				}
//				console.debug("pop2:", acc.join("."));
				tree=acc.pop().node;
				
			}								
		}, path);
		return tree;
	};

	
	function buildNodeTree(source, schema, fnBuildNodeCallback){
		var tree = objectToNodeTree(source, function(obj, path){
			
			var fnBuildNode = fnBuildNodeCallback || function(config, data, path){
//				console.debug("Nodepath:", _.map(path, "key").join("."));
				return {
					nodeName: config.label || path[path.length-1].key,					
					nodeConfig: config, 
					nodeData: data,
					nodePath: _.map(path, "key").join("."),
					nodeParent: path.length>1 ? path[path.length-2].node : undefined,
					nodes:[]};
			}
			
			var nodePath=_.map(path, "key").join(".");
			if(Array.isArray(obj)){
				console.debug("buildNodeTree length", obj.length, nodePath);
			}
//			console.debug("nodePath", nodePath, path)
			var nodeDef = _.find(schema, function(value, key){
//				console.debug("buildNodeTree:", key, nodePath, nodePath.match(key+"$"))
				return (nodePath.match(key+"$")!==null);	
			});	
//			
			if(nodeDef){
				console.debug("nodeDef", nodeDef)
				if(nodeDef.skip){
					return path[path.length-1].node;
				}else{
					//				console.info("path2: ", nodePath, obj);
					var newNode = fnBuildNode(nodeDef, obj, path);
					//				var newNode = {name: path[path.length-1].key, nodes:[]};
					
					path[path.length-1].node.nodes.push(newNode);					
					return newNode;
				}
								
			}
//			console.log("notp2:", nodePath, obj);
//			return fnBuildNode({}, obj, path);
			return undefined;
		});
		console.info("NODETREE", tree);
		return tree;
	}
	
	function createNodeTree(obj, callback, tree, limit){
		var tree={node: name, nodes:[]};

		_.transform(obj, function(acc, value, key, obj){
			if(_.isObject(value)){
				acc.push(key);
				console.debug("push:", acc.join("."));
				callback(value, acc);
				walkObjectTree(value, callback, acc);
				console.debug("pop:", acc.join("."));
				path.pop();	
				
			}else if(_.isArray(value)){
				acc.push(key);
				console.debug("push:", acc.join("."));				
				_.forEach(value, function(item){
						callback(item, acc)					
						walkObjectTree(item, callback, acc);
						console.debug("pop:", acc.join("."));
						path.pop();	
						
				});								
				acc.pop();
				console.debug("pop:", acc.join("."));
			}else if(!_.isFunction(value) && !_.isUndefined(value) && !_.isEmpty(value) && !_.isNull(value)){
				acc.push(key);
				console.debug("push:", acc.join("."));
				callback(value, acc);					
				walkObjectTree(value, callback, acc);
				console.debug("pop:", acc.join("."));
				path.pop();	
				
			}								
		}, path);
		
	};
	
	function traverse1(obj, callback, path){
		
		var path = path || [];
		//remember the current path
		path.push(obj);
		//do work
		callback(obj, path);
		
		//find child objects
		var chldren = _.filter(_.values(obj), _.isObject);
		if(chldren.length>0){	
			_.map(chldren, function(child){							
				traverse1(child, callback, path);
				path.pop();					
			});
		}else{					
			return obj;
		}
	};
	
	function traverse(obj, callback, path){						
		if(!path){
			//the very first object
			path=[];
			callback(obj, path);				
		}
		
		_.mapValues(obj, function(value, key, obj){				
			//find child objects				
			if(_.isObject(value)){					
				//remember the current path
				path.push(key);
				//do work
				callback(value, path);
				
				traverse(value, callback, path);
				path.pop();
			}				
		});
	};


	function traverseRev(obj, callback, path){
		var isRoot=false;
		if(!path){
			//the very first object
			path=[];
			isRoot=true
		}
		
		_.mapValues(obj, function(value, key, obj){				
			//find child objects				
			if(_.isObject(value)){					
				//remember the current path
				path.push(key);				
				traverseRev(value, callback, path);
				
				//do work
				callback(value, path);				
				path.pop()
			}				
		});
		
		//process the root node
		if(isRoot){
			callback(obj, path);
		}
	};
	
	function getProp(objPath){
		return _.reduce(objPath, function(result, prop){				
			return result[prop];
		}, data);
	}
	

	function urlToObj(objPath, data){		
		return _.reduce(objPath, function(result, prop){				
			return result[prop];
		}, data);
	};
	
	
	
	function urlToArray(objPath, data, fnGetId){
		fnGetId = fnGetId || function(obj){return obj.name};
		return _.reduce(objPath, function(result, prop){
			if(prop==="") return result;
			try{
				if(_.isArray(result)){
					var found = _.find(result, function(item){
						return String(fnGetId(item))===prop;
					});
//					if(!found){
//						var errContext=[result,prop];					
//						var errMsg="Error finding object in array [result, prop]: ";
//						console.error(errMsg, errContext);
////						throw new Error(errMsg+JSON.stringify(errContext));
//					}
					return found;
				}else{
					return result[prop];
				}
			}catch(e){
				errContext = [objPath, data, prop, result, e];
				errMsg = "Error mapping urlToArray from [objPath, data, result, prop, e]:";
				console.error(errMsg, errContext);
				throw new Error(errMsg+JSON.stringify(errContext));
			}
			
		}, data);
	};
	
	return {
		traverse: traverse,
		traverse1: traverse1,
		traverseRev: traverseRev,
		urlToObj: urlToObj,
		urlToArray: urlToArray,
		sleep: sleep,
		walkObjectTree: walkObjectTree,
		walkObjectTree2: walkObjectTree2,
		objectToNodeTree: objectToNodeTree,
		buildNodeTree: buildNodeTree
	};
	
});