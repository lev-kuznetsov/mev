define(["mev-analysis", "./IntegerParam.tpl.html", "../label/ParamLabelDirective"], function(mod, template){
	function mevIntegerParamDirective(){
		return {
			restrict: "EAC",
			template: template,
			scope: {
				param: "=mevParam"
			}
		}
	}
	mevIntegerParamDirective.$inject=[];
	mevIntegerParamDirective.$name="mevIntegerParamDirective";
	return mevIntegerParamDirective;
})