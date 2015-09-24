define([], function(){
	var DashboardLayoutService = function DashboardLayoutService(){		
		this.panels={};	
	};
	DashboardLayoutService.$name="DashboardLayout";
	DashboardLayoutService.provider="service";	
	DashboardLayoutService.$inject=[];
	return DashboardLayoutService;
});