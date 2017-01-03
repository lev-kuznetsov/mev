define(['ng'], function (ng){
			
		PresetListVMFactory = function (){
			return function PresetListVM(dataPromise) {
				var _self = this;				
				dataPromise.then(function(tcgaPresets){
					_self.presets=tcgaPresets;				
				});
				
				this.gridOptions={ 
					data: 'PresetsListVM.presets',
					columnDefs: [{field: 'name', displayName: 'Dataset', enableCellEdit: false, width:'40%',
						cellTemplate: '<div><div class="ngCellText"><a data-toggle="modal" role="button" data-target="#import-presets-modal" href="" ng-click="PresetsListVM.showImportPreset(row.getProperty(\'name\'))">{{row.getProperty(col.field)}}</a></div></div>'},
						{field:'disease', displayName:'Desease Name', enableCellEdit: false, visible: false},
						{field:'dataLevelNorm', displayName:'Data Level', enableCellEdit: false, width:'10%'},
						{field:'platformName', displayName:'Platform', enableCellEdit: false, width:'45%'}
						],
						groups: ['disease'],
						groupsCollapsedByDefault: true,
						sortInfo: { fields: ['disease', 'dataLevelNorm'], directions: ['asc', 'asc'] },
						showFilter: true,
						selectWithCheckboxOnly: true ,
						enableRowSelection: false
				};
				this.importPresetUrl = "about:blank";
				this.showImportPreset = function(presetName){						
					_self.importPresetUrl="/annotations/import-dataset/command/core/view-preset-sample-annotations?import-preset="+presetName+"&dimension=column";
				};
			};
		};
		PresetListVMFactory.$inject=[];
	    PresetListVMFactory.$name="PresetsListVM";
	    PresetListVMFactory.$provider="factory";
		return PresetListVMFactory;
});