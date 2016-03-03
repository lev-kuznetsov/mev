define(["lodash"], function(_){
	return function DecimalParam(spec){
		_.assign(this, {type: "decimal"}, spec);
	}
})