define(['jquery', 'angular'], function(jquery, angular){
	OpenRefineBridge = {
		addSelectionSet: function(selection){
			console.debug('in addSelectionSet');
			console.debug("selection", selection);
			
			var elm = document.querySelector('#selectionSetMgr');			
			var selectionSetMgrDOM = angular.element(elm);
			
			var rootScope = angular.element(document).scope();
			if(rootScope.$state){				
				rootScope.$state.go("^."+selection.dimension.toLowerCase()+"Set", {setId: selection.name});
			}
			
			var selectionSetManagerScope=selectionSetMgrDOM.scope();			
			console.debug("selectionSetManagerScope:", selectionSetManagerScope);
			if(selectionSetManagerScope){
				selectionSetManagerScope.addItem(selection);
			}else{
				rootScope.$broadcast("mui:dataset:selections:added", selection.dimension.toLowerCase(), selection);
			}
			
		},
		loadedProject: function(theProject){
			var rootScope = angular.element(document).scope();;
			console.debug("loadedProject", theProject);
			if(theProject.metadata.customMetadata.dimension==="COLUMN")
				rootScope.$broadcast("openRefine:loadedAnnotations:column", theProject);
			else if(theProject.metadata.customMetadata.dimension==="ROW")
				rootScope.$broadcast("openRefine:loadedAnnotations:row", theProject);
			else
				rootScope.$broadcast("openRefine:loadedAnnotations", theProject);
		},
		openDataset: function(dataset){
			console.debug('in openDataset');
			console.debug("dataset:"+angular.toJson(dataset));		
			jquery('#import-presets-modal').modal('hide');	
			jquery('div.modal-backdrop').hide();
//			setTimeout(function(){
			var rootScope = angular.element(document).scope();
			if(rootScope.$state){				
				rootScope.$state.go("root.dataset.home", {datasetId: dataset.name});
				return;
			}
			
			var datasetUrl = "/#/dataset/"+dataset.name+"/";					
			window.location.replace(datasetUrl);
//			}, 500);
			
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