define(["lodash", "app/utils/utils"], function(_,utils){
	var ProjectTreeAdaptor = function ProjectTreeAdaptor(DatasetProjectTreeSchema, DatasetProjectTreeEventBus){
		
		return function(project){
			var tree={name: project.name, nodes:[]};				
			
			tree=utils.buildNodeTree(project, DatasetProjectTreeSchema, function(config, data, path){
				console.debug("Nodepath:", _.map(path, "key").join("."));
				return {
					nodeName: config.label || path[path.length-1].key,					
					nodeConfig: config, 
					nodeData: obj,
					nodePath: _.map(path, "key").join("."),
					nodeParent: path.length>1 ? path[path.length-2].node : undefined,
					nodes:[]
				};				
			});
			
			console.debug("ProjectTREE", tree);
			return tree;
		};
	};
	
	ProjectTreeAdaptor.$inject=["DatasetProjectTreeSchema", "DatasetProjectTreeEventBus"];
	return ProjectTreeAdaptor;
});