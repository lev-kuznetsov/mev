define(["angular", "angular-utils-ui-breadcrumbs"], function(angular){	
	var module=angular.module("Mev.MainMenuModule", ["angularUtils.directives.uiBreadcrumbs"]);
//	module.config(["uiBreadcrumbsTemplateProvider", function(uiBreadcrumbsTemplateProvider){
//		console.debug("uiBreadcrumbsTemplateProvider");
//		uiBreadcrumbsTemplateProvider.setPath('/container/vendor/mbAngularUtils/breadcrumbs/origBreadcrumbs.tpl.html');
//	}]);
	module.path="/container/javascript/mainmenu/";
	return module;
});