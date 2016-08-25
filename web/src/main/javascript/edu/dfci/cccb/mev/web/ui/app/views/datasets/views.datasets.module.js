define(["mui",
	"./_controllers/DatasetsVM",
	"./_templates/views.datasets2.tpl.html",
	"./_templates/views.datasets.tutorials.tpl.html",
	"./_templates/views.datasets.google.tpl.html",
	"./_templates/views.datasets.upload.tpl.html",
	"./_templates/views.datasets.geods.tpl.html",
	"./_templates/views.datasets.imports.tpl.html",
	"../../widgets/presets/widgets.presets.module",
	"./session/views.datasets.session.module",
		"mev-bs-modal",
		"mev-workspace",
		'js-data-angular',
		"../../domain/domain.module",
		'ng-grid',
		'blob-util',
		"geods"],
function(ng, DatasetsVM, datasetsTemplate, tutorialsTemplate, googleTemplate, uploadTemplate, geodsTemplate, importsTemplate){
	var module = ng.module("mui.views.datasets", ["Mev.GeodsModule","ngGrid"], arguments);
	module.config(['$stateProvider', '$urlRouterProvider', "$$animateJsProvider",
	     	function($stateProvider, $urlRouterProvider, $$animateJsProvider){
				$urlRouterProvider.when("/datasets", "/datasets/workspace");
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
						parent: "root",
						displayName: "datasets",
						template: datasetsTemplate,
						redirectTo: "root.datasets.imports.upload"
					})
					.state("root.datasets.imports", {
						url: "",
						parent: "root.datasets",
						displayName: false,
						views: {
							"imports": {
								template: "<div ui-view></div>"
							}
						},
						sticky: true,
						onEnter: ["mevFetchSrc", function(mevFetchSrc) {
							return mevFetchSrc.fetch("app/views/datasets/views.datasets.module", $$animateJsProvider)
								.catch(function(e){
									throw e;
								});
						}]
					})
					.state("root.datasets.imports.tutorials", {
						url: "/tutorials",
						parent: "root.datasets.imports",
						template: tutorialsTemplate,
						displayName: "tutorials"
					})
					.state("root.datasets.imports.upload", {
						url: "/upload",
						parent: "root.datasets.imports",
						template: uploadTemplate,
						displayName: "upload"
					})
					.state("root.datasets.imports.tcga", {
						url: "/tcga",
						parent: "root.datasets.imports",
						template: "<div id=\"presetMgr\" presets-list></div>",
						displayName: "TCGA"
					})
					.state("root.datasets.imports.geods", {
						url: "/geods",
						parent: "root.datasets.imports",
						template: geodsTemplate,
						displayName: "GEO"
					})
					.state("root.datasets.imports.google", {
						url: "/google",
						parent: "root.datasets.imports",
						template: googleTemplate,
						displayName: "Google Drive"
					});
	}]);
	// module.controller("DatasetsVM", DatasetsVM);
	return module;
});
