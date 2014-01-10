define(['angular'], function(angular){
	
	angular.module( 'Mev.SetManager', [])
		.directive('selectionSetManager', [function (){
			  return {				  		  
				  scope: {
					  heatmapId: '@heatmapId',
					  heatmapData: '=heatmapData'
				  },
				  restrict : 'EA',
				  templateUrl : '/container/view/elements/setmanager/selectionSetManager'
			  };
		}])
		.directive('selectionSetList', [function (){
			return {
				scope: {
					selections: '=mevSelections'
					, baseUrl: '@mevBaseUrl'
					, setSelected: '&mevSayHello'
				},
				restrict : 'EA',
				transclude : true,
				replace : true,
				controller: 'SelectionSetManagerCtl',
				templateUrl : '/container/view/elements/setmanager/selectionSetList',
				link : function (scope, iElement, iAttrs, controller){
					scope.sayHelloDir = function(){
						alert("link: " + scope.heatmapId + ":" + scope.selections.length + ":" + scope.$id);
					};					
				}				
			};
		}])
		.directive('selectionSetEditForm', [function (){
			return {
				restrict: 'EA',
				replace: true,
				require: '^selectionSetList',
				templateUrl : '/container/view/elements/setmanager/selectionSetEditForm',				
			};
		}])
		.controller('SelectionSetManagerCtl', ['$scope', '$element', '$attrs', function($scope, $element, $attrs){
			
			$scope.sayHelloCtl = function() {
				alert($scope.heatmapId + ":" + $scope.heatmapData.column.selections.length + ":" + $scope.$id);
			};		
			
			alert(angular.toJson($attrs) );
						
			$scope.selectedItem = null;
			$scope.selectedItemTmp = null;
			
			$scope.setSelected = function(item){
				$scope.selectedItem = item;
				$scope.selectedItemTmp = angular.copy(item);
				alert("selected: " + item.name);
			};
			$scope.saveItem = function(item){
				alert("save: " + item.name + " old: " + $scope.selectedItem.name);
				$scope.selectedItem.name = item.name;
				$scope.selectedItem.properties = item.properties;
			};
		}]);
});

