define(["lodash", "../BaseParam"], function(_, BaseParam){
	function IntegerParam(spec){
		_.assign(this, {type: "integer"}, spec);
	}
	IntegerParam.prototype = new BaseParam();
	return IntegerParam;
})