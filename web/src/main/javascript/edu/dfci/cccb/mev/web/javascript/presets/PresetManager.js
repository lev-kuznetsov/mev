define(['jquery', 'angular'], function (jquery, angular){
	angular.module('Mev.PresetManager', [])
		.controller('PresetManagerController', ['$scope', '$element', '$attrs', 'PresetService', function($scope, $element, $attrs, presetService){			
			$scope.presets = presetService.getPresetList();
			$scope.orderByColumn="diseaseName";
			$scope.orderByReverse=false;
		}])
		.service('PresetService', ['QHTTP', 'alertService', function(QHTTP, alertService){
			this.name="Antony";
			this.getPresetList = function(){
                var params = {
                  method : 'GET',
                  url : '/presets/tcga?format=json'
                };
                return QHTTP (
                    params,
                    function (d, s) {
                      return d;
                    },
                    function (d, s) {
                      var message = "Could not pull your dataset list If "
                          + "problem persists, please contact us."

                      var header = "Could Not Pull List Of Datasets (Error Code: "
                          + s + ")"
                      alertService.error (message, header);
                    });

              
			};
		}])
		.directive('presetManager', [function(){
			return {
				link: function(scope){
					
				},
				restrict: 'EA',
				templateUrl: '/container/view/elements/presets/presetList'
			};
		}]);		
});