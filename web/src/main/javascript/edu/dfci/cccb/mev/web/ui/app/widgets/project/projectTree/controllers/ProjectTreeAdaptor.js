"use strict";
define(["mui", "lodash", "app/utils/utils"], function(ng,_,utils){
	var ProjectTreeAdaptor = function ProjectTreeAdaptor(DatasetProjectTreeSchema, DatasetProjectTreeEventBus, $state){
		
		return function(project){
			var tree={name: project.name, nodes:[]};				
			
			tree=utils.buildNodeTree(project, DatasetProjectTreeSchema, function(config, data, path){
				console.debug("Nodepath:", _.map(path, "key").join("."));
				return {
					nodeName: config.label || path[path.length-1].key,					
					nodeConfig: config, 
					nodeData: data,
					nodePath: _.map(path, "key").join("."),
					nodeParent: path.length>1 ? path[path.length-2].node : undefined,
					activate: function(){
						var node = this;
						var params = node.nodeConfig.state.getParams(node);						
						var targetState = "root"+node.nodeConfig.state.name;
						console.debug("node.activate", node, $state, params, targetState);

						$state.go(targetState, params);
					},
					nodes:[]
				};				
			});
			
			console.debug("ProjectTREE", tree);
			return tree;
		};
	};
	
	ProjectTreeAdaptor.$inject=["DatasetProjectTreeSchema", "DatasetProjectTreeEventBus", "$state"];
	return ProjectTreeAdaptor;
});