define(['jquery', 'mui'], function (jquery, angular, template){
	angular.module('Mev.PresetManager', ["mui.domain.presets.tcga"])		
		.controller('PresetManagerController', ['$scope', '$element', '$attrs', function($scope, $element, $attrs){			
			$scope.gridOptions = { 
					data: 'presets',
					columnDefs: [{field: 'name', displayName: 'Dataset', enableCellEdit: false, width:'40%',
									cellTemplate: '<div><div class="ngCellText"><a data-toggle="modal" role="button" data-target="#import-presets-modal" href="" ng-click="showImportPreset(row.getProperty(\'name\'))">{{row.getProperty(col.field)}}</a></div></div>'},
					             {field:'disease', displayName:'Desease Name', enableCellEdit: false, visible: false},
					             {field:'dataLevel', displayName:'Data Level', enableCellEdit: false, width:'10%'},			                     
			                     {field:'platformName', displayName:'Platform', enableCellEdit: false, width:'45%'}
			                     ],
			        groups: ['diseaseName'],
			        groupsCollapsedByDefault: true,
			        sortInfo: { fields: ['diseaseName', 'dataLevel'], directions: ['asc', 'asc'] },
			        showFilter: true,
			        selectWithCheckboxOnly: true ,
			        enableRowSelection: false
				};

		}])
		.directive('presetManager', ['TcgaPreset', function(TcgaPreset){
			return {
				link: function(scope){
											
					TcgaPreset.findAll().then(function(tcgaPresets){
						scope.presets=tcgaPresets;				
					});						
					scope.importPresetUrl="about:blank";
					scope.showImportPreset = function(presetName){						
						scope.importPresetUrl="/annotations/import-dataset/command/core/view-preset-sample-annotations?import-preset="+presetName+"&dimension=column";
					};										
				},
				restrict: 'EA',
				templateUrl: '/container/view/elements/presets/presetList',
				template: template
					
			};
		}]);
});