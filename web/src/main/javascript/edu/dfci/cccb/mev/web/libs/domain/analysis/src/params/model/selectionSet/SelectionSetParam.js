define(["lodash", "../select/SelectParam"], function(_, SelectParam){
	"use strict";
	function SelectionSetParamFactory(mevSelectionLocator){
		return function(spec){

			_.assign(this, new SelectParam(
				_.assign(this, spec, {
						type: "select",
						options: mevSelectionLocator.find.bind(this, spec.dimension)
					})
				)
			);

		};
	}
	SelectionSetParamFactory.$name="mevSelectionSetParam";
	SelectionSetParamFactory.$provider="factory";
	SelectionSetParamFactory.$inject=["mevSelectionLocator"];
	return SelectionSetParamFactory;
});