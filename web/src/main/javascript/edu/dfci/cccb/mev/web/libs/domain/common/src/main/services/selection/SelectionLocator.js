define(["lodash"], function(_){
	function mevSelectionLocator(mevContext){		
		this.find = function(dimension, level, param){
			var context = mevContext.current() || mevContext.root();
			if(level)
				context = mevContext.get(level);
			else
				level = mevContext.getLevel() || "root";
			if(!context) return [];
			if(level==="root"){				
				return mevContext.root().dataset.selections[dimension];
			}else {
				if(context.type){
					var selections = [];
					if(context.params && context.params.control && context.params.experiment ){
						var name = _.isObject(context.params.control)
							? context.params.experiment.name + "+" + context.params.control.name
							: context.params.experiment + "+" + context.params.control
						var unionSet = {
							name: name,
							keys: []
						};
						_.transform(mevContext.root().dataset.selections[dimension], function(result, selection, index){
							if(context.params.dimension === dimension && (selection.name === context.params.control || selection.name === context.params.experiment)){
								result.keys = _.union(result.keys, selection.keys);
							}
						}, unionSet);
						if(unionSet.keys.length > 0 ){
							selections.push(unionSet);
						}
					}
					if(context.getFilteredKeys){
						var filteredKeys = context.getFilteredKeys(dimension);
						if(filteredKeys && filteredKeys.length > 0){
							selections.push({
								name: "current filter",
								keys: filteredKeys
							});
						}
					}
					if(context.getOriginalInputKeys){
						var filteredKeys = context.getOriginalInputKeys(dimension);
						if(filteredKeys && filteredKeys.length > 0){
							selections.push({
								name: filteredKeys.displayName || "original "+dimension+" input",
								keys: filteredKeys
							});
						}
					}
					if(param && param.id==="experiment" && context.getExperiment){
						selections.push(context.getExperiment());
					}
					if(param && param.id==="control" && context.getControl){
						selections.push(context.getControl());
					}
					if(context.getSelections){
						context.getSelections().map(function(selection){
							selection.selected=selection.checked || selection.selected;
							selections.push(selection);
						});
					}
					return selections;
				}else{
					return mevContext.root().dataset.selections[dimension];
				}
			}

			 

		};
		this.row = function(){				
			return this.find("row");
		};
		this.column = function(){
			return this.find("column");
		};
	}
	mevSelectionLocator.$inject=["mevContext"];
	mevSelectionLocator.$name="mevSelectionLocator";
	mevSelectionLocator.$provider="service";
	return mevSelectionLocator;
});