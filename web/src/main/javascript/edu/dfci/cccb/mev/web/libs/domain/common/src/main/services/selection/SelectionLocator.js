define([], function(){
	function mevSelectionLocator(mevContext){		
		this.find = function(dimension){
			if(dimension)
				return mevContext.root().dataset.selections[dimension]
			else
				return mevContext.root().dataset.selections
		}
		this.row = function(){				
			return this.find("row");
		};
		this.column = function(){
			return this.find("column");
		}		
	}
	mevSelectionLocator.$inject=["mevContext"];
	mevSelectionLocator.$name="mevSelectionLocator";
	mevSelectionLocator.$provider="service";
	return mevSelectionLocator;
});