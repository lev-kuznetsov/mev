"use strict";
define(["ng"], function(ng){
	var SideMenuVM = function($scope, mevAnalysisTypes){
		this.analysisTypes = mevAnalysisTypes.all();
		console.debug("analysisTypes", this.analysisTypes);
		this.hi="hiiii";
	};
	SideMenuVM.$injcet=["$scope", "mevAnalysisTypes"];
	return SideMenuVM;
});