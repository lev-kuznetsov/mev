define(['jquery', 'angular'], function (jquery, angular){
	angular.module('Mev.PresetManager', [])
		.controller('PresetManagerController', ['$scope', '$element', '$attrs', 'PresetService', function($scope, $element, $attrs, presetService){			
			presetService.getPresetList().then(function(d){$scope.presets=d;});
			$scope.orderByColumn="name";
			$scope.orderByReverse=false;
			$scope.importPresetUrl="about:blank";
			$scope.showImport=false;			
		}])
		.service('PresetService', ['QHTTP', 'alertService', function(QHTTP, alertService){
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
					scope.showImportPreset = function(presetName){						
						scope.importPresetUrl="/annotations/import-dataset/command/core/view-preset-annotations?import-preset="+presetName;
						scope.showImport=true;
					};					
					scope.showPresetList = function(){
						scope.importPresetUrl="about:blank";
						scope.showImport=false;
					};
				},
				restrict: 'EA',
				templateUrl: '/container/view/elements/presets/presetList'
			};
		}]);		
});