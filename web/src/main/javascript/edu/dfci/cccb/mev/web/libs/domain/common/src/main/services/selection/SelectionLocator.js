define(["lodash"], function(_){
	function mevSelectionLocator(mevContext){		
		this.find = function(dimension){
			var context = mevContext.current() || mevContext.root();
			if(mevContext.getLevel()==="root"){				
				return mevContext.root().dataset.selections[dimension];
			}else if(context.type){
				if(context.params && context.params.control && context.params.experiment ){					
					var unionSet = {
						name: context.params.experiment + "+" + context.params.control,
						keys: []
					};
					_.transform(mevContext.root().dataset.selections[context.params.dimension || dimension], function(result, selection, index){
						if(selection.name === context.params.control || selection.name === context.params.experiment){
							result.keys = _.union(result.keys, selection.keys);
						}
					}, unionSet);
					return[unionSet];
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