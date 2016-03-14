define([], function(){
	var ProjectNodeDirective = function ProjectNodeDirective($compile, $rootScope){
		
		function initBootstrapTree(){
			console.debug("INITIT Bootstrap TREE");
			$('.tree > ul').attr('role', 'tree').find('ul').attr('role', 'group');
			$('.tree').find('li:has(ul)').addClass('parent_li').attr('role', 'treeitem').find(' > span').attr('title', 'Collapse this branch').on('click', function (e) {
		        var children = $(this).parent('li.parent_li').find(' > ul > li');
		        if (children.is(':visible')) {
		    		children.hide('fast');
		    		$(this).attr('title', 'Expand this branch').find(' > i').addClass('glyphicon-plus-sign').removeClass('glyphicon-minus-sign');
		        }
		        else {
		    		children.show('fast');
		    		$(this).attr('title', 'Collapse this branch').find(' > i').addClass('glyphicon-minus-sign').removeClass('glyphicon-plus-sign');
		        }
		        e.stopPropagation();
		    });			
		}
		
		var templateMap = {
			Datasets: "<div>{{node.nodeName}}</div>",
			Analyses: "<div>{{node.nodeName}}</div>",
			"Sample Sets": "<column-node node='node'></column-node>",
			"Gene Sets": "<row-node node='node'></row-node>",
			"Analysis": "<analysis-node node='node'></analysis-node>",
			"selectionSet": "<selection-set-node node='node'></selection-set-node>",
//			Dataset: "<div ng-click='console.debug(\"dataset.click\")'>{{node.nodeName}}</div>",
//			Analysis: "<project-analysis analysis=\"node.nodeData\"></project-analysis>",			
			Default: "<project-node-default node='node' ></project-node-default>"
		};
		
		return {
			restrict: "AE",
			scope: {
				name: "&",
				data: "=",
				config: "&",
				children: "=",
				node: "="
			},
			replace: true,
			template: "<span  id='{{vm.id}}'  ng-class='{selected: vm.isSelected(), disabled: vm.isDisabled(), error: vm.isError()}' class='nodeWrapper node-{{node.nodenode.name || node.nodeName}}' ng-click='vm.onClick($event)' ng-dblclick='vm.toggle($event)'></span>",
//			templateUrl: "app/widgets/project/projectTree/_projectNode/templates/projectNodeDefault.bootstrapTree.tpl.html",
//			templateUrl: "app/widgets/project/projectTree/_projectNode/templates/projectNodeDefault.tpl.html",			
			require: "^projectTree",
			compile: function(elm, attr){
				link = {
                    post: function (scope, elm, attr, ctrl) {
//                    	console.debug("ui:projectNode:post-link", scope, elm, attr, ctrl);
                    	scope.vm={
                    			id: scope.node.nodePath,
                    			toggle: function(e){
//                    				alert(scope.node.nodeName);
                    				console.debug("node.toggle", scope.node.nodeName);
                    				var nodeWrapper=$(e.target).closest('.nodeWrapper'); 
                    				var parent = nodeWrapper.parent('li.parent_li');
                			        var children = parent.find(' > ul > li');
                			        if (children.is(':visible')) {
                			    		children.hide('fast');
                			    		nodeWrapper.attr('title', 'Expand this branch').find(' > i').addClass('glyphicon-plus-sign').removeClass('glyphicon-minus-sign');
                			        }
                			        else {
                			    		children.show('fast');
                			    		nodeWrapper	.attr('title', 'Collapse this branch').find(' > i').addClass('glyphicon-minus-sign').removeClass('glyphicon-plus-sign');
                			        }                			                        			        
                    			    e.stopPropagation();                    			    
                    			},
                    			isSelected: function(){
                    				return ctrl.getSelected()===scope.node.nodePath;
                    			},
                    			isDisabled: function(){
                    				return scope.node.nodeData.status && (scope.node.nodeData.status === "IN_PROGRESS");
                    			},
                    			isError: function(){
                    				return scope.node.nodeData.status && scope.node.nodeData.status === "ERROR";
                    			},
                    			onClick: function(e){
//                    				if(this.isDisabled())
//                    					return;
                    				ctrl.toggleSelected(scope.node.nodePath);
                    				console.debug("node.click, ui:projectTree:nodeSelected", scope.node.nodeName);                    				
                    				$rootScope.$broadcast("ui:projectTree:nodeSelected", scope.node);
                    			}
                    	};
                    	 
                    	console.debug("Post LINK node: ", scope.node.nodeName, scope.node.nodeConfig.type, scope.node);
//                    	scope.$watch("node.nodeData", function(newVal, oldVal){
//                        	if(newVal && !oldVal){
//                        		scope.$emit("ui:projectTree:dataChanged");
//                        	}
//                        }, true);
                    	
                    	var template = templateMap[scope.node.nodeName] || templateMap[scope.node.nodeConfig.type] || templateMap.Default;
                    	template="<i class='nodeToggle glyphicon glyphicon-minus-sign' ng-click='vm.nodeclick($event)'></i>"+template;
                    	elm.html(template);                    	
//                    	elm.after("<span class=\"nodeAction\"><a>hi</a></span>")
//  border: solid 1px #999;
//  padding: 4px;
//  border-radius: 20px;
//  margin-left: 5px;
                        $compile(elm.contents())(scope);                           
                    }
				};
				return link;
			}
			
		};
	};
	ProjectNodeDirective.$inject=["$compile", "$rootScope"];
	return ProjectNodeDirective;
});