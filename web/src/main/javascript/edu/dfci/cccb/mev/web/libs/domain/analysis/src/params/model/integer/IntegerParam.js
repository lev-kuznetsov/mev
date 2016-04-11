define(["lodash"], function(_){
	return function IntegerParam(spec){
		_.assign(this, {type: "integer"}, spec);
	}
})