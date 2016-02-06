"use strict";
define(["lodash", "../select/SelectParam"], function(_, SelectParam){
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
	SelectionSetParamFactory.$name="mevSelectionSetParamFactory";
	SelectionSetParamFactory.$provider="factory";
	SelectionSetParamFactory.$inject=["mevSelectionLocator"];
	return SelectionSetParamFactory;
});