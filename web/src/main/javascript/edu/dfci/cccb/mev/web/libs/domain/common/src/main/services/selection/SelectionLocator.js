define(["lodash"], function(_){
	function mevSelectionLocator(mevContext){		
		this.find = function(dimension){
			var context = mevContext.current() || mevContext.root();
			if(!context) return [];
			if(mevContext.getLevel()==="root"){				
				return mevContext.root().dataset.selections[dimension];
			}else if(context.type){
				var selections = [];
				if(context.params && context.params.control && context.params.experiment ){
					var unionSet = {
						name: context.params.experiment + "+" + context.params.control,
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
				return selections;
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