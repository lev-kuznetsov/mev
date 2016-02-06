"use strict";
define(["mev-project/data/mouse_test_data.tsv.json", "mev-project"], 
function(mouseJson){
	function value(mevProject){		
		return mevProject("mouse_test_data.tsv", mouseJson);
	}
	value.$inject=["mevProject"];
	value.$name="mevMockProject";
	value.$provider="service";
	return value;
});