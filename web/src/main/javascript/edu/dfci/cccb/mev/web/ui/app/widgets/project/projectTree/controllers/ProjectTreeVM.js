define(["ng", "./RecursiveTreeMixin"], 
function(ng, RecursiveTreeMixin) {
	var ProjectTreeVM = function ProjectTreeVM(ProjectTreeAdaptor, $modal, $scope, $timeout, AnalysisEventBus) {
//		RecursiveTreeMixin.call(this);		
		$scope["delete"] = function(data) {
	        data.nodes = [];
	    };
	    $scope.add = function(data, nodeSubType) {
	        var post = data.nodes.length + 1;
	        var newName = data.name + '-' + post;
	        data.nodes.push({name: newName+"-"+nodeSubType,nodes: []});
	    };
	    
//	    $scope.tree = [{name: "prefix", nodes: []}, {name: $scope.nodeType, nodes: [{name: "Item 1"}, {name: "Item 2"}]}];
//	    var thepromise = $scope.project.$promise || $scope.project;
//	    thepromise.then(
//	    		function(response){
//	    			console.debug("project view response:", response);
//	    			$scope.tree=[ProjectTreeAdaptor(response)];
//	    		})
//	    $scope.rootNode={name: "root", dataset:[{name: "node1", result:[{name: "result1.1"}, {name: "result1.2"}]}, {name: "node2", dataset: [{name: "node2.1"}]}]};
	    console.debug("ProjectTreeAdaptor", $scope.project);
	    $scope.tree=ProjectTreeAdaptor($scope.project).nodes;
	    $scope.script_id="tree_item_renderer.html";
//	    $scope.script_id=$scope.nodeType+".html";
	    
	    $scope.$on('$includeContentLoaded', function($scope){
//	        console.debug("*$includeContentLoaded", $scope);	        
	    });
	    
	    
		$scope.selected={
				id: undefined
		};
		this.getSelected=function(){
			return $scope.selected.id;
		};
		this.toggleSelected=function(id){
			console.debug("projectNode:ctrl:toggleSelected", $scope.selected.id, id);
			$scope.selected.id=id;
		};
		$scope.$on("ui:projectTree:dataChanged", function(){
			console.debug("on ui:projectTree:dataChanged");
			$timeout(function(){
				$scope.tree=ProjectTreeAdaptor($scope.project).nodes;
			});
		});		
	};
	
	ProjectTreeVM.$inject=["ProjectTreeAdaptor2", "$modal", "$scope", "$timeout", "mevAnalysisEventBus"];
	return ProjectTreeVM;
});