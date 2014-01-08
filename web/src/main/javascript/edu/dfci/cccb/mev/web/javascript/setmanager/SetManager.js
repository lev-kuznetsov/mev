define(['angular'], function(angular){
	
	angular.module( 'Mev.SetManager', [])
		.directive('selectionSetManager', [function (){
			  return {				  
				  require: ['^heatmapData', '^heatmapId'],		  
				  scope: {
					  heatmapId: '@heatmapId',
					  heatmapData: '=heatmapData'
				  },
				  controller : 'SelectionSetManagerCtl',				  
				  restrict : 'EA',
				  templateUrl : '/container/view/elements/setmanager/selectionSetManager'
			  };
		}])
		.directive('selectionSetList', [function (){
			return {
				scope: {
					selections: '=mevSelections',
					baseUrl: '@mevBaseUrl',
					sayHello: '&mevSayHello'
				},
				require : '^SelectionSetManagerCtl',				  
				restrict : 'EA',
				transclude : true,
				replace : true,
				templateUrl : '/container/view/elements/setmanager/selectionSetList'
			};
		}])
		.controller('SelectionSetManagerCtl', ['$scope', function($scope){
			$scope.sayHello = function() {
				alert($scope.heatmapId + ":" + $scope.heatmapData.column.selections.length);
			};			
		}]);
});

