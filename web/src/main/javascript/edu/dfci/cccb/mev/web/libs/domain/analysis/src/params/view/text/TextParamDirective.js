define(["mev-analysis", "./TextParam.tpl.html", "../label/ParamLabelDirective"], function(mod, template){
	function mevTextParamDirective(){
		return {
			restrict: "EAC",
			template: template,
			scope: {
				param: "=mevParam"
			}
		}
	}
	mevTextParamDirective.$inject=[];
	mevTextParamDirective.$name="mevTextParamDirective";
	return mevTextParamDirective;
})