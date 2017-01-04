define(["lodash", "../select/SelectParam", "../BaseParam"], function(_, SelectParam, BaseParam){
	"use strict";
	function SelectionSetParamFactory(mevSelectionLocator){
		function SelectionSetParam(spec){
			SelectParam.call(this, spec);
			_.assign(this, {
				options: mevSelectionLocator.find.bind(this, spec.dimension, undefined, this)
			});

			this.validate = function (values){
				if(this.max)
					if(this.value && _.isArray(this.value.keys) && this.value.keys.length > this.max)
						return this.id + " size may not exceed " + this.max;
			};
		};
		SelectionSetParam.prototype = new SelectParam();
		return SelectionSetParam;
	}
	SelectionSetParamFactory.$name="mevSelectionSetParam";
	SelectionSetParamFactory.$provider="factory";
	SelectionSetParamFactory.$inject=["mevSelectionLocator"];
	return SelectionSetParamFactory;
});