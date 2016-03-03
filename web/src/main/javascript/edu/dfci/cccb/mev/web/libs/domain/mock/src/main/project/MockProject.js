define(["mev-project/data/mouse_test_data.tsv.json", "mev-project"], 
function(mouseJson){ "use strict";
	function value(mevProject){		
		var project = mevProject("mouse_test_data.tsv", mouseJson);
		return project;
	}
	value.$inject=["mevProject"];
	value.$name="mevMockProject";
	value.$provider="service";
	return value;
});