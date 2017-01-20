define(["mui", "angular-ui-router",
	"./services/context/Context",
	"./services/selection/SelectionLocator",
	"./services/analysis/AnalysisLocator",
	"./services/annotations/AnnotationsLocator",
	"./services/db/mevDb",
	"./services/settings/Settings",
	"./view/db/status/mevDbStatusDirective"
	], function(ng){ "use strict";
	return ng.module("mevDomainCommon", arguments, arguments);
});