define(['angular'], function(angular){
	
	angular.module( 'Mev.SetManager', [])
		.directive('selectionSetManager', [function (){
			  return {				  
				  require: '^heatmapData',
				  scope: {
					  heatmapData: '=heatmapData'
				  },
				  controller : 'SelectionSetManagerCtl',				  
				  restrict : 'EA',
				  templateUrl : '/container/view/elements/setmanager/selectionSetManager'
			  };
		}])
		.controller('SelectionSetManagerCtl', ['$scope', function($scope){
			$scope.sayHello = function() {
				alert($scope.heatmapData.column.selections.length);
			};			
		}]);
	
});

