define(["lodash", "../BaseParam"], function(_, BaseParam){
	function NameParam(spec){
		_.assign(this, spec, {
			displayName: "Name",
			id: "name",
			type: "text",
			required: true
		});
	}
	NameParam.prototype = new BaseParam();
	return NameParam;
});