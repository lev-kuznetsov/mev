define([], function(){
	function mevSelectionLocator(mevContext, MevAnnotationRepository){		
		this.find = function(dimension){			
			var dataset = mevContext.root().dataset;
			// if(!dataset.annotations)	
			// 	dataset.annotations={};
			// if(!dataset.annotations[dimension])
			// 	dataset.annotations[dimension] = new MevAnnotationRepository(dimension);			
			// return dataset.annotaions[dimension];
			return dataset.getAnnotations(dimension);
		};
		this.row = function(){				
			return this.find("row");
		};
		this.column = function(){
			return this.find("column");
		};		
	}
	mevSelectionLocator.$inject=["mevContext", "mevAnnotationRepository"];
	mevSelectionLocator.$name="mevAnnotationsLocator";
	mevSelectionLocator.$provider="service";
	return mevSelectionLocator;
});