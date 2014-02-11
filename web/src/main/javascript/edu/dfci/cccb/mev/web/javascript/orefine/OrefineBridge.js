define(['jquery', 'angular'], function(jquery, angular){
	OpenRefineBridge = {
		addSelectionSet: function(selection){
			console.log('in addSelectionSet');
			console.log("selection:"+angular.toJson(selection));
			
			var elm = document.querySelector('#selectionSetMgr');			
			var selectionSetMgrDOM = angular.element(elm);
			
			var selectionSetManagerScope=selectionSetMgrDOM.scope();
			console.log("selectionSetManagerScope:"+angular.toJson(selectionSetManagerScope));			
			selectionSetManagerScope.addItem(selection);
			
		},
		openDataset: function(dataset){
			console.log('in openDataset');
			console.log("dataset:"+angular.toJson(dataset));			
			var datasetUrl = "/#/dataset/"+dataset.name+"/";			
			window.location.replace(datasetUrl);
		},
		showPresetList: function(){
			var elm = document.querySelector('#presetMgr');			
			var presetMgrDOM = angular.element(elm);
			
			var presetManagerScope=presetMgrDOM.scope();
			console.log("presetManagerScope:"+angular.toJson(presetManagerScope));			
			presetManagerScope.showPresetList();			
			presetManagerScope.$apply();
		}
	
		
	};
	return OpenRefineBridge;
});