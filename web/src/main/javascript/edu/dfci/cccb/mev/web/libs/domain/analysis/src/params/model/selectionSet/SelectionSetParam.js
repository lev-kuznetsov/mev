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

			this.validate = function (values){
				if(this.max)
					if(this.value && _.isArray(this.value.keys) && this.value.keys.length > this.max)
						return this.id + " size may not exceed " + this.max;
			};
		};
	}
	SelectionSetParamFactory.$name="mevSelectionSetParam";
	SelectionSetParamFactory.$provider="factory";
	SelectionSetParamFactory.$inject=["mevSelectionLocator"];
	return SelectionSetParamFactory;
});