define(["lodash"], function(_){
	return function TextParam(spec){
		_.assign(this, {type: "text"}, spec);
	}
})