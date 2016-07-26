define(["lodash", "../BaseParam"], function(_, BaseParam){
	function DecimalParam(spec){
		_.assign(this, {type: "decimal"}, spec);
	}
	DecimalParam.prototype = new BaseParam();
	return DecimalParam;
})