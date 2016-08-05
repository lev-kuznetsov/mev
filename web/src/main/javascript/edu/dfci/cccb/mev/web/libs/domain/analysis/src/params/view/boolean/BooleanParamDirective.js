define(["mev-analysis", "./BooleanParam.tpl.html", "../label/ParamLabelDirective"], function(mod, template){
	function mevBooleanParamDirective(){
		return {
			restrict: "EAC",
			template: template,
			scope: {
				param: "=mevParam"
			}
		}
	}
	mevBooleanParamDirective.$inject=[];
	mevBooleanParamDirective.$name="mevBooleanParamDirective";
	return mevBooleanParamDirective;
})