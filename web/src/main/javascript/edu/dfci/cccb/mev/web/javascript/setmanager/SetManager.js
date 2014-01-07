define(['angular'], function(angular){
	
	angular.module( 'MEV.SetManager', [])
		.directive('selectionSetManager', [function (){
			  return {
				  restrict : 'EA',        		  
				  templateUrl : '/container/view/elements/setmanager/selectionSetManager'        		  
			  };
		}]);
	
});

