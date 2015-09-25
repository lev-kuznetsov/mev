define(["ng", "lodash"], function(ng, _){
	var AnyAnalysisDirective = function AnyAnalysisDirective(AnalysisTypes, $state, $resolve, $injector){
		
		
		
		function resolveProject($state){
			return $state.$current.locals["@root.dataset"].project;
		}
		function resolveAnalysis(analysisName){
			var project = resolveProject($state);
			var analysis = _.find(project.dataset.analyses, function(analysis){
				return analysis.name === analysisName;
			});
			return analysis;
		}
		
		return {
			restrict: "AE",
			scope: {
				anyAnalysis: "@",
				anyTemplateUrl: "@",
				anyViewModel: "@" 
			},
			template: function(tElement, tAttrs){
				console.debug("anyAnalysis template", tAttrs);
				return "<div ng-include=\"getTemplateUrl()\" onload=\"processAnalysis()\">"+tAttrs.anyAnalysis+"</div>";				
			},
//			templateUrl: function(tElement, tAttrs){				
//				var templateUrl="app/views/dataset/analysis/default/view.analysis.default.tpl.html";
//				
//				var analysis = resolveAnalysis(tAttrs.anyAnalysis);				
//				var analysisType = AnalysisTypes[analysis.type];				
//				if(analysisType && analysisType.shortName){
//					templateUrl=templateUrl.replace("default", analysisType.shortName).replace("default", analysisType.shortName);
//				}
//				console.debug("anyAnalysis templateUrl:", templateUrl, analysisType, tAttrs, $state, resolveProject($state), analysis);
//				return templateUrl;
//			},
			controllerAs: "DatasetAnalysisVM",
			controller: ["$scope",  function($scope){
				console.debug("anyAnalysis ctrl:", $scope);
				var project = resolveProject($state);
				var analysis = resolveAnalysis($scope.anyAnalysis);
				
				var analysisType;
				var ctrlName;
				if($scope.anyViewModel){
					ctrlName = $scope.anyViewModel;
				}else{
					analysisType = AnalysisTypes[analysis.type];
					ctrlName = analysisType.viewModel+"Factory";
					console.debug("anyAnalysis ctrlName:", ctrlName, analysisType, $state, resolveProject($state), analysis);
				}
				$scope.DatasetAnalysisVM.project=project;				
				$scope.DatasetAnalysisVM.analysis=analysis;
				$scope.getTemplateUrl=function(){
					if($scope.anyTemplateUrl){
						return $scope.anyTemplateUrl; 
					}
					
					var templateUrl="app/views/dataset/analysis/default/view.analysis.default.tpl.html";						
					if(analysisType && analysisType.shortName){
						templateUrl=templateUrl.replace("default", analysisType.shortName).replace("default", analysisType.shortName);
					}
					console.debug("anyAnalysis templateUrl:", templateUrl, analysisType, $state, resolveProject($state), analysis);
					return templateUrl;
				};				
				if($injector.has(ctrlName)){					
					var ctrl = $injector.get(ctrlName); 
					console.debug("any controller", ctrlName, ctrl);
					$injector.invoke(ctrl, $scope.DatasetAnalysisVM, {$scope: $scope, project: project, analysis: analysis});
//					ctrl.call(this, $scope, project, analysis);	
				}
				
			}],
			compile: function(tElm, tAttr){				
				console.debug("anyAnalysis compile", tElm, tAttr);
				return function(scope, elm, attr, controller){
					console.debug("anyAnalysis link", scope, attr);
					
					scope.processAnalysis=function(){
						if(!controller.analysis) return;
						console.debug("processAnalysis", elm);						
						elm.find("[href]").each(function(){							
							var href = this.attributes.getNamedItem("href").value;
							if(href && href.startsWith("#")){
								var newId = controller.analysis.name+"-"+href.substr(1);								
								console.debug("id ...", href, newId);	
								this.href="#"+newId;								
								elm.find(href).each(function(){
									console.debug("... changing", this.id, newId);
									this.id=newId;
								});								
							}							
						});
					}
				} 
			}			
			
		};
	};
	AnyAnalysisDirective.$name="anyAnalysisDirective";
	AnyAnalysisDirective.$inject=["AnalysisTypes", "$state", "$resolve", "$injector"];
	return AnyAnalysisDirective;
});