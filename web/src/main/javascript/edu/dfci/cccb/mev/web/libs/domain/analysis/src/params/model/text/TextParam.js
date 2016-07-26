define(["lodash", "../BaseParam"], function(_, BaseParam){
	function TextParam(spec){
		_.assign(this, {type: "text"}, spec);
	}
	TextParam.prototype = new BaseParam();
	return TextParam;
})