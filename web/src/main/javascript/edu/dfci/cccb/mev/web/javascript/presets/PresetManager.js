define(['jquery', 'angular'], function (jquery, angular){
	angular.module('Mev.PresetManager', [])		
		.controller('PresetManagerController', ['$scope', '$element', '$attrs', 'PresetService', function($scope, $element, $attrs, presetService){			
			$scope.gridOptions = { 
				data: 'presets',
				columnDefs: [{field:'diseaseName', displayName:'Desease Name', enableCellEdit: false, visible: false},
				             {field:'dataLevel', displayName:'Data Level', enableCellEdit: false, width:'10%',
								cellTemplate: '<div><div class="ngCellText"><a href="" ng-click="showImportPreset(row.getProperty(\'name\'))">{{row.getProperty(col.field)}}</a></div></div>',
				            	 },			                     
		                     {field:'platformName', displayName:'Platform', enableCellEdit: false, width:'45%'},
				             {field: 'name', displayName: 'Dataset', enableCellEdit: false, width:'40%'}
		                     ],
		        groups: ['diseaseName'],
		        groupsCollapsedByDefault: false,
		        sortInfo: { fields: ['diseaseName', 'dataLevel'], directions: ['asc', 'asc'] },
		        showFilter: true
			};						
			presetService.getPresetList().then(function(d){
				$scope.presets=d;				
			});						
			$scope.orderByColumn="name";
			$scope.orderByReverse=false;
			$scope.importPresetUrl="about:blank";
			$scope.showImport=false;
			$scope.data={levels: ["Level_3", "Level_2", ]};
			$scope.filter={
					levels: ["Level_3", "Level_2"],
					level: "Level_3"
			};
			
			$('#importTabs').on('click', function (e) {                
                var elPresetsGrid=$('#presetsGrid');
                elPresetsGrid.trigger('resize');
                elPresetsGrid.css("wdith", "100%");
                var elPresetsGridWrapepr=$('.mev-preset-list-wrapper');
                elPresetsGridWrapepr.trigger('resize');
            });
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
                          + "problem persists, please contact us.";

                      var header = "Could Not Pull List Of Datasets (Error Code: "
                          + s + ")";
                      alertService.error (message, header);
                    });              
			};
		}])		
		.directive('presetManager', [function(){
			return {
				link: function(scope){
					
					scope.showImportPreset = function(presetName){						
						scope.importPresetUrl="/annotations/import-dataset/command/core/view-preset-sample-annotations?import-preset="+presetName+"&dimension=column";
						scope.showImport=true;
					};					
					scope.showPresetList = function(){
						scope.importPresetUrl="about:blank";
						scope.showImport=false;
					};
					scope.getFilterLevel2 = function(){						
						if(scope.filter.level2.include)
							return "Level_2";
						else
							return "";
					};					
				},
				restrict: 'EA',
				templateUrl: '/container/view/elements/presets/presetList'
			};
		}])
		.filter('togglableFilter',[function(){
		    return function(input, filterText, isFilterActive){
		        if(!angular.isDefined(isFilterActive) || !isFilterActive) return input;
		        var ret = [];
		        if(angular.isDefined(filterText) && angular.isString(filterText)){
		            angular.forEach(input, function(v){
		                if(v.diseaseName.indexOf(filterText) !== -1){
		                    ret.push(v);
		                }
		            });
		        }
		        return ret;
		    }
		}])
		.filter('checkboxFilter',[function(){
		    return function(input, checkboxes){		        
		        var ret = [];		        
		        if(angular.isDefined(checkboxes) && angular.isArray(checkboxes)){
		            angular.forEach(input, function(v){
		            	for(i=0;i<checkboxes.length;i++){
		                	if(checkboxes[i]!=="" && v.name.indexOf(checkboxes[i]) !== -1){
		                		ret.push(v);
		                	}
		                }
		            });
		        }
		        return ret;
		    }
		}]);
});