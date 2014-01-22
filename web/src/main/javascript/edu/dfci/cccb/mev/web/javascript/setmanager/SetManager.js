define(['jquery','angular'], function(jquery, angular){
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
					, dimension: '@mevDemintion'
				},
				restrict : 'EA',
				transclude : true,
				replace : true,
				controller: 'SelectionSetManagerCtl',
				templateUrl : '/container/view/elements/setmanager/selectionSetList',
				link : function (scope, iElement, iAttrs, controller){
					scope.sayHelloDir = function(){
						//alert("link: " + scope.heatmapId + ":" + scope.selections.length + ":" + scope.$id);
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
		.directive('myIframe', function(){
		    var linkFn = function(scope, element, attrs) {
		        element.find('iframe').bind('load', function (event) {
		          //event.target.contentWindow.scrollTo(0,400);
		        });
		        
		    };
		    return {
		      restrict: 'EA',
		      scope: {
		        ngSrc:'@ngSrc',
		        height: '@height',
		        width: '@width',
		        scrolling: '@scrolling',
		        annotationsUrl: '=annotationsUrl'
		      },
		      template: '<iframe scrolling="no" class="frame" height="{{height}}" width="{{width}}" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="{{scrolling}}" ng-src="{{annotationsUrl}}"></iframe>',
		      link : linkFn
		    };
		  })
		.controller('SelectionSetManagerCtl', ['$scope', '$element', '$attrs', function($scope, $element, $attrs){
			
			$scope.sayHelloCtl = function() {
				//alert($scope.heatmapId + ":" + $scope.heatmapData.column.selections.length + ":" + $scope.$id);
			};		
			
			//alert(angular.toJson($attrs) );
						
			$scope.selectedItem = null;
			$scope.selectedItemTmp = null;
			$scope.annotationsUrl="hello";
			
			$scope.setSelected = function(item){
				$scope.selectedItem = item;
				$scope.selectedItemTmp = angular.copy(item);
				//alert("selected: " + item.name);
			};
			$scope.saveItem = function(item){
				//alert("save: " + item.name + " old: " + $scope.selectedItem.name);
				$scope.selectedItem.name = item.name;
				$scope.selectedItem.properties = item.properties;
			};
			$scope.showAnnotations = function(selection, dimention){
				//alert(angular.toJson(selection));
				$scope.$emit('ViewAnnotationsEvent', selection, dimention);				
				
			}
			$scope.addItem = function(item){
				//alert('in addItem');				
				$scope.$apply(function(){
					
					if(item.dimension.toLowerCase()=="column"){
						$scope.heatmapData.column.selections = jquery.grep($scope.heatmapData.column.selections, function(e, i){return e.name==item.name}, true);
						$scope.heatmapData.column.selections.push(item);
					}else{
						$scope.heatmapData.row.selections = jquery.grep($scope.heatmapData.row.selections, function(e, i){return e.name==item.name}, true);
						$scope.heatmapData.row.selections.push(item);						
					}
					
				});
			}
			
			
		}]);
});

