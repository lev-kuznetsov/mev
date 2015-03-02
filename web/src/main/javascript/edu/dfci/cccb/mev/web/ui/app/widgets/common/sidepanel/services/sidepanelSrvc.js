
define(["ng"], function(ng){
	var SidePanelSrvc = function SidePanelSrvc(){		
			var sidePanelCollapse={
					left: false,
					right: false
			};
			function toggle(direction){			
				sidePanelCollapse[direction]= !sidePanelCollapse[direction];
				console.debug("sidepanel "+direction+" toggled to " + sidePanelCollapse[direction]);
			}
			function isCollapsed(direction){
				return sidePanelCollapse[direction];
			}		
			
			this.toggle=toggle;
			this.isCollapsed=isCollapsed;
			
	};
	SidePanelSrvc.$inject=[];
	return SidePanelSrvc;
});