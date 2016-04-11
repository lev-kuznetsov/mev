define(["./heatmapSettingsModalBody.tpl.html"], function(template){
	function directive(){
		return {
	        restrict : 'E',
	        scope : {
	            currentColors : '=currentColors',
	            availableColorGroups : '=availableColorGroups',
	            colorEdge : '=colorEdge',
	            applyNewRanges : '=applyNewRanges',
	            applyDefaultRanges : '=applyDefaultRanges',
	            heatmapView: "=heatmapView"
	        },            
	        link: function(scope, elm, attr){
	            scope.currentColors.group = d3colors.current();
	        },
	        template : template
	    };
	}

	directive.$name="mevHeatmapSettings";
	directive.$provider="directive";
	return directive;
});