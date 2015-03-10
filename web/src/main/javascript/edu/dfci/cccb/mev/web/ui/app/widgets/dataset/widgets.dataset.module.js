define(["ng", "./datasetList/widgets.datasetlist.module", 
        "./datasetLabel/widgets.datasetLabel.module",
        "./column/widgets.dataset.column.module",
        "./row/widgets.dataset.row.module",
        "./datasetNode/widgets.dataset.datasetNode.module"], 
function(ng){	
	var module = ng.module("mui.widgets.dataset", ["mui.widgets.datasetlist", 
	                                               "mui.widgets.datasetlabel",
	                                               "mui.widgets.dataset.DatasetNode",
	                                               "mui.widgets.dataset.column",
	                                               "mui.widgets.dataset.row"
	                                               ]);			
	return module;
});