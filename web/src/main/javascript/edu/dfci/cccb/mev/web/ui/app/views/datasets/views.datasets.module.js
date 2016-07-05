define(["mui",
	"./_controllers/DatasetsVM",
	"./_templates/views.datasets.tutorials.tpl.html",
	"./_templates/views.datasets.google.tpl.html",
	"../../widgets/presets/widgets.presets.module",
	"./session/views.datasets.session.module"],
function(ng, DatasetsVM, tutorialsTemplate, googleTemplate){
	var module = ng.module("mui.views.datasets", arguments, arguments);
	module.config(['$stateProvider', '$urlRouterProvider',
	     	function($stateProvider, $urlRouterProvider){
				$urlRouterProvider.when("/datasets", "/datasets/tcga");
	     		$stateProvider
					.state("root.datasetsOld", {
						url: "/datasetsOld",
						controller: "ImportsCtrl",
						controllerAs: "ImportsCtrl",
						parent: "root",
						templateUrl: "app/views/datasets/_templates/views.datasets.tpl.html",
						displayName: "datasets"
					})
					.state("root.datasets", {
						url: "/datasets",
						controller: "ImportsCtrl",
						controllerAs: "ImportsCtrl",
						parent: "root",
						templateUrl: "app/views/datasets/_templates/views.datasets2.tpl.html",
						displayName: "datasets"
					})
					.state("root.datasets.tutorials", {
						url: "/tutorials",
						parent: "root.datasets",
						template: tutorialsTemplate,
						displayName: "tutorials"
					})
					.state("root.datasets.tcga", {
						url: "/tcga",
						parent: "root.datasets",
						template: "<div id=\"presetMgr\" presets-list></div>",
						displayName: "TCGA"
					})
					.state("root.datasets.geods", {
						url: "/geods",
						parent: "root.datasets",
						template: "<div class='row'><div class='col-md-12' id=\"geodsImportMgr\" mev-geods-import-directive></div></div>",
						displayName: "GEO"
					})
					.state("root.datasets.google", {
						url: "/google",
						parent: "root.datasets",
						template: googleTemplate,
						displayName: "Google Drive"
					});;
	}]);
	// module.controller("DatasetsVM", DatasetsVM);
	return module;
});
