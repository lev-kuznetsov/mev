define(["lodash", "../select/SelectParam", "../BaseParam"], function(_, SelectParam, BaseParam){
	"use strict";
	function SelectionSetParamFactory(mevSelectionLocator){
		function SelectionSetParam(spec){
			SelectParam.call(this, spec);
			_.assign(this, {
				options: mevSelectionLocator.find.bind(this, spec.dimension, undefined, this)
			});

			this.validate = function (values){
				if(this.max)
					if(this.value && _.isArray(this.value.keys) && this.value.keys.length > this.max)
						return this.id + " size may not exceed " + this.max;
				if(this.disjoint){
					if(!this.multiselect)
						if(this.value && _.isArray(this.value.keys)){
							var disjointTarget = values[this.disjoint];
							if(disjointTarget && _.isArray(disjointTarget.keys)){
								var intersect = _.intersection(disjointTarget.keys, this.value.keys);
								if(intersect.length > 0)
									return this.id + " and " + this.disjoint + " must be disjoint sets but have "
										+ intersect.length
										+ " element in common: "
										+ intersect.slice(0, 9).join(",")
										+ (intersect.length>10 ? "..." : "");


							}
						}
					if(this.multiselect){
						var value = this.getValue();
						if(_.isArray(value)){
							for(var i=0; i<value.length; i++){
								for(var j=i+1; j<value.length; j++){
									var intersect = _.intersection(value[i].keys, value[j].keys);
									if(intersect.length > 0)
										return "Selections  must be disjoint sets but "
											+ value[i].name + " and " + value[j].name
											+ " have "
											+ intersect.length
											+ " element in common: "
											+ intersect.slice(0, 9).join(",")
											+ (intersect.length>10 ? "..." : "");
								}
							}
						}
					}
				}

			};
		};
		SelectionSetParam.prototype = new SelectParam();
		return SelectionSetParam;
	}
	SelectionSetParamFactory.$name="mevSelectionSetParam";
	SelectionSetParamFactory.$provider="factory";
	SelectionSetParamFactory.$inject=["mevSelectionLocator"];
	return SelectionSetParamFactory;
});