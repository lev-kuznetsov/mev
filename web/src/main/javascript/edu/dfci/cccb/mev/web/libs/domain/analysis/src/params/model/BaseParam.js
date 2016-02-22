define(["mui"], function(ng){ "use strict";
	function BaseParam(){
		this.validate = function (){
			if(this.required===true && !this.value)
				return this.id + " is required";
		};
	}
	BaseParam.$injcect=[];
	BaseParam.$name="BaseParam";
	BaseParam.$provider="factory";
	return BaseParam;
});