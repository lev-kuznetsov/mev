define(["mui", "mev-dataset/src/main/dataset/lib/AnalysisClass", "./demo.tpl.html", "../data/mev_test_data.hcl_cols.json", "mev-heatmap", "mev-mock", "angular-ui-router",
	"bootstrap", "bootstrap/dist/css/bootstrap.min.css", "jquery-ui-bundle/jquery-ui.css"], 
function(ng, AnalysisClass, template, hclJson){

	var demo = ng.module("demo", arguments, arguments).directive("mevBlah", function(){
		return {
			restrict: "AEC",
			template: "<div>drown</div>"
		};
	}).run(["$state", "mevMockProject", function($state, mevMockProject){
		mevMockProject.dataset.analyses.push(new AnalysisClass(hclJson));
		// $state.go("root.dataset.analysisType.pe", {datasetId: mevMockProject.dataset.id, analysisId: hclJson.name});
		$state.go("mock-heatmap");
	}]).config(["$stateProvider", function($stateProvider){


		$stateProvider.state("mock-heatmap", {			
			template: "<button ng-click=\"vm.refresh()\">refresh</button>"
				+ "<a mev-svg-save-as=\"{name: 'heatmap-demo', selector: 'mev-heatmap svg'}\">save</a>"
				+ "<mev-heatmap mev-heatmap-View=\"vm.heatmapView\" mev-dataset=\"vm.project.dataset\" ></mev-heatmap>",
			controller: ["mevMockProject", function(project){
				var analysis = project.dataset.analyses.filter(function(item){
					return item.name==="hcl_cols";
				})[0];
				this.analysisId=analysis.name;				 
				this.analysis = analysis;
				this.project=project;
				
				var labels = traverse(analysis.root);
				function traverse(tree){
		            var leaves = {
		                '0': [],
		                '1': []
		            };

		            if (tree.children.length > 0) {
		                for (var i = 0; i < tree.children.length; i++) {
		                    leaves[i] = (!tree.children[i].children) ? [tree.children[i].name] : traverse(tree.children[i]);
		                }
		            }

		            return leaves[0].concat(leaves[1]);
		        }

				this.heatmapView = project.generateView({
		            viewType:'heatmapView', 
		            labels:{
		                row:{keys:project.dataset.row.keys}, 
		                column:{keys:labels}
		            },
		            expression:{
		                min: project.dataset.expression.min,
		                max: project.dataset.expression.max,
		                avg: project.dataset.expression.avg,
		            },
	                panel: {
	                    top: analysis
	                }
		        });

		        this.refresh=function(){
					this.heatmapView = project.generateView({
			            viewType:'heatmapView', 
			            labels:{
			                row:{keys:project.dataset.row.keys}, 
			                column:{keys:labels}
			            },
			            expression:{
			                min: project.dataset.expression.min,
			                max: project.dataset.expression.max,
			                avg: project.dataset.expression.avg,
			            },
		                panel: {
		                    top: analysis
		                }
			        });		        	
		        };
			}],
			controllerAs: "vm"
		});
	}]);

	ng.element(document).ready(function(){
		ng.bootstrap(document, [demo.name]);
	});
});