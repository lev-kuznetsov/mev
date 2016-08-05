define(["lodash"], function(_){ "use strict";
	function BaseParam(spec){
		_.assign(this, spec);
		this.validate = function (){
			if(this.required===true && !this.value)
				return this.id + " is required";
		};
		this.checkConstraint = function(params){
			var _self = this;
			var params = params || this.params;
			if(!this.constraint)
				return true;

			var constraintParam = _.find(params, function(param){
				return _self.constraint.paramId === param.id;
			});
			if(!constraintParam)
				return true;

			if(constraintParam){
				return constraintParam.value === this.constraint.value;
			}
		};
	}
	BaseParam.$injcect=[];
	BaseParam.$name="BaseParam";
	BaseParam.$provider="factory";
	return BaseParam;
});