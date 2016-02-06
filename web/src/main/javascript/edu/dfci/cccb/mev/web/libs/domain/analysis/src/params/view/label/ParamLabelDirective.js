define(["mui", "lodash", "./ParamLabel.tpl.html"], function(ng, _, template){
	function mevParamLabelDirective(){
		return {
			restrict: "AC",
			template: template,
			scope: {
				param: "=mevParam"				
			}
		}
	}
	mevParamLabelDirective.$inject=[];
	mevParamLabelDirective.$name="mevParamLabelDirective";
	return mevParamLabelDirective;
})