define(["lodash", "../BaseParam"], function(_, BaseParam){
	function BooleanParam(spec){
		_.assign(this, {type: "boolean"}, spec);
	}
	BooleanParam.prototype = new BaseParam();
	return BooleanParam;
})