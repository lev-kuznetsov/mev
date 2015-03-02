define(["ng", "./projectList/widgets.projectlist.module", 
        "./projectLabel/widgets.projectLabel.module",
        "./projectMenuList/widgets.projectMenuList.module",
        "./projectTree/widgets.projectTree.module",
        "./projectData/widgets.projectData.module",
        "./projectDataList/widgets.projectDataList.module",
        "./projectDataDimension/widgets.projectDataDimension.module",
        "./projectNav/widgets.projectNav.module",
        "./projectDataSelectionList/widgets.projectDataSelectionList.module",
        "./projectAnalysisList/widgets.projectAnalysisList.module",
        "./projectAnalysis/widgets.projectAnalysis.module",
        "./projectAnalysisParameters/widgets.projectAnalysisParameters.module",
        "./projectAnalysisResultList/widgets.projectAnalysisResultList.module",
        "./projectAnalysisResult/widgets.projectAnalysisResult.module"],
function(ng){	
	var module = ng.module("mui.widgets.project", ["mui.widgets.projectlist", 
	                                               "mui.widgets.projectlabel",
	                                               "mui.widgets.projecttree",
	                                               "mui.widgets.projectDataList",
	                                               "mui.widgets.projectData",
	                                               "mui.widgets.projectNav",
	                                               "mui.widgets.projectDataDimension",
	                                               "mui.widgets.projectDataSelectionList",
	                                               "mui.widgets.projectAnalysisList",
	                                               "mui.widgets.projectAnalysis",
	                                               "mui.widgets.projectAnalysisParameters",
	                                               "mui.widgets.projectAnalysisResultList",
	                                               "mui.widgets.projectAnalysisResult"
//	                                               , "mui.widgets.projectMenuList"
	                                               ]);			
	return module;
});