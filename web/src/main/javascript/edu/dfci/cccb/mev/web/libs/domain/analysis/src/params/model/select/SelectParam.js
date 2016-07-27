define(["lodash", "../BaseParam"], function(_, BaseParam){
	function SelectParam(spec){
		var _self = this;

		// var options = spec.options;
		// if(!_.isFunction(options)){
		// 	spec.options = function(){
		// 		return options;
		// 	};
		// }
		BaseParam.call(_self, spec);
		_.assign(
			_self,
			{
				type: "select",
				getValue: this.multiselect
					? getMultiValue
					: getValue
			});

		function getValue(){
			return this.value && this.bound
				? this.value[this.bound]
				: this.value
		}
		function getMultiValue(){
			return _self.options()
				.filter(function(option){
					return option.selected;
				})
				.map(function(option){
					return _self.bound
						? option[_self.bound]
						: option
				});
		}
	};
	SelectParam.prototype = new BaseParam();
	SelectParam.prototype.constructor = SelectParam;
	SelectParam.prototype.getOptions = function(){
		if(!this.options)
			throw new Error("SelectParam: options not defined");

		if(_.isFunction(this.options))
			return this.options();
		else
			return this.options;
	}
	return SelectParam;
});