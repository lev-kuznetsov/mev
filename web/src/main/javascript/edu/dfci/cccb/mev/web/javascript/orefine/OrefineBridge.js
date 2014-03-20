define(['jquery', 'angular'], function(jquery, angular){
	OpenRefineBridge = {
		addSelectionSet: function(selection){
			console.debug('in addSelectionSet');
			console.debug("selection:"+angular.toJson(selection));
			
			var elm = document.querySelector('#selectionSetMgr');			
			var selectionSetMgrDOM = angular.element(elm);
			
			var selectionSetManagerScope=selectionSetMgrDOM.scope();
			console.debug("selectionSetManagerScope:"+angular.toJson(selectionSetManagerScope));			
			selectionSetManagerScope.addItem(selection);
			
		},
		openDataset: function(dataset){
			console.debug('in openDataset');
			console.debug("dataset:"+angular.toJson(dataset));			
			var datasetUrl = "/#/dataset/"+dataset.name+"/";			
			window.location.replace(datasetUrl);
		},
		showPresetList: function(){
			var elm = document.querySelector('#presetMgr');			
			var presetMgrDOM = angular.element(elm);
			
			var presetManagerScope=presetMgrDOM.scope();
			var presetManagerController=presetMgrDOM.controller();
			console.debug("presetManagerScope:"+angular.toJson(presetManagerScope));
			console.debug("presetManagerScope.orderByColumn:"+presetManagerScope.orderByColumn);
			
			console.debug("presetManagerController.orderByColumn:"+presetManagerController.orderByColumn);
						
			presetManagerScope.showPresetList();
			presetManagerScope.$apply();
			
//			presetManagerScope.$parent.$$nextSibling.$$childHead.$$nextSibling.$$nextSibling.showPresetList();	
//			presetManagerScope.$apply(function(){
//				presetManagerScope.showPresetList();
//			});
			
		}
	
		
	};
	return OpenRefineBridge;
});