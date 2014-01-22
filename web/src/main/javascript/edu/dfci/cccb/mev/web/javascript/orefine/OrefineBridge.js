define(['jquery', 'angular'], function(jquery, angular){
	OpenRefineBridge = {
		addSelectionSet: function(selection){
			console.log('in addSelectionSet');
			console.log("selection:"+angular.toJson(selection));
			
			var elm = document.querySelector('#selectionSetMgr');
			
			var selectionSetMgrDOM = angular.element(elm);
			
			//var selectionSetManagerCtl=selectionSetMgrDOM.controller(); 
			//angular.element(domElement).controller()
			//console.log("selectionSetManagerCtl:"+selectionSetManagerCtl);
			//selectionSetManagerCtl.addItem(selection);
			
			var selectionSetManagerScope=selectionSetMgrDOM.scope();
			console.log("selectionSetManagerScope:"+angular.toJson(selectionSetManagerScope));			
			selectionSetManagerScope.addItem(selection);
			
		}
	};
	return OpenRefineBridge;
});