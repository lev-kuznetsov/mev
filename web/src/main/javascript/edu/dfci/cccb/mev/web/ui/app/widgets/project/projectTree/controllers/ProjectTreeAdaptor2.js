define(["mui", "lodash", "app/utils/utils"], function(ng,_,utils){ "use strict";
	var ProjectTreeAdaptor2 = function ProjectTreeAdaptor2(DatasetProjectTreeSchema, DatasetProjectTreeEventBus, $state){
		function getInheritedParams(defaults, node){
			var params = defaults || {};
			if(node.nodeParent && node.nodeParent.nodeConfig){
				_.extend(params, node.nodeParent.nodeConfig.state.getParams(node.nodeParent));
			}						
			return params;
		}
		function getNodePath(node){
			if(!node.parent)
				return node.nodeName;
			else
				return node.parent.path + "." + node.nodeName;				
		}
		function createNode(data, pConfig, parent){		
			var config = pConfig || {};
			return _.assign(
				Object.create({}, {
					nodePath: {
						configurable: false,
						get: function(){ return getNodePath(this);}					
					}
				}),
				{
					nodeName: config.label || data.name || data.id,					
					nodeConfig: config || {}, 
					nodeData: data,
					nodeParent: parent,
					activate: function(){
						var node = this;
						var params = node.nodeConfig.state.getParams(node);						
						var targetState = "root"+node.nodeConfig.state.name;
						console.debug("node.activate", node, $state, params, targetState);
						$state.go(targetState, params);
					},
					add: function(node){
						node.parent = this;					
						this.nodes.push(node);
					},
					child: function(name){
						return _.find(this.nodes, {nodeName: name});
					},
					descendant: function(name, node){
						if(!node) node = this;
						var result = node.child(name);
						if(result) return result;
						
						for(var i=0;i<node.nodes.length;i++){
							var cur = node.nodes[i];							
							result = node.descendant(name, cur);
							if(result)
								return result;
						}
						
					},
					nodes:[]
				}
			);		
		}

		return function(project){
			var projectNode=createNode(project);				
			
			var datasetNode = createNode(project.dataset, {
				label: "Dataset", 
				state: {
					name: ".dataset.home", 
					getParams: function(node){
						return {datasetId: node.nodeData.id, node: node};
					}
				}
			});
			projectNode.add(datasetNode);

			var columnSetsNode = createNode(project.dataset.column, {
				label: "Sample Sets", 
				state: {
					name: ".dataset.columnSets", 
					getParams: function(node){
						return {};
					}
				}
			});
			datasetNode.add(columnSetsNode);
			project.dataset.column.selections.forEach(function(selection){
				var selectionNode = createNode(selection, {
					type: "selectionSet",
					state: {
						name: ".dataset.columnSet",
						getParams: function(node){
							return {setId: node.nodeData.name};
						}
					}
				});
				columnSetsNode.add(selectionNode);
			});

			var rowSetsNode = createNode(project.dataset.row, {
				label: "Gene Sets",
				state: {
					name: ".dataset.rowSets", 
					getParams: function(node){
						return {};
					}
				}
			});
			datasetNode.add(rowSetsNode);
			project.dataset.row.selections.forEach(function(selection){
				var selectionNode = createNode(selection, {
					type: "selectionSet",
					state: {
						name: ".dataset.rowSet",
						getParams: function(node){
							return {setId: node.nodeData.name};
						}
					}
				});
				rowSetsNode.add(selectionNode);
			});

			var analysesNode = createNode(project.dataset.analyses, {
				label: "Analyses",
				state: {
					name: ".dataset.analyses",
					getParams: function(node){}
				}
			});
			datasetNode.add(analysesNode);

			_.transform(_.sortBy(project.dataset.analyses, "name"), function(node, analysis, index, analyses){
				var analysisNode = createNode(analysis, {
					type: "Analysis",
					state: {
						name: ".dataset.analysis",
						getParams: function(node){
							return getInheritedParams({analysisId: node.nodeData.name, analysisType: node.nodeData.type}, node);
						},
						isDisabled: function(node){
							return node.nodeData.status==='IN_PROGRESS';
						}
					}
				});

				var parentNode = node;
				var prevAnalysis = _.find(analyses, function(item){					
					return analysis.name.indexOf(item.name+".")===0;
				});
				if(prevAnalysis)
					parentNode = node.descendant(prevAnalysis.name);

				parentNode.add(analysisNode);
			}, analysesNode);

			console.debug("ProjectTREE", projectNode);
			return projectNode;
		};
	};
	
	ProjectTreeAdaptor2.$inject=["DatasetProjectTreeSchema", "DatasetProjectTreeEventBus", "$state"];
	ProjectTreeAdaptor2.$name="ProjectTreeAdaptor2";
	return ProjectTreeAdaptor2;
});