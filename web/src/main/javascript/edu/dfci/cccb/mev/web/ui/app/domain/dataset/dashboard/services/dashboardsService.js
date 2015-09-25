define([], function(){
	var Dashboards = function Dashboards(DashboardItems){
		var _self = this;
		this.$add = function(item){
			_self[item.name] = item;
		};
		this.$new = function(id){
			
		};
	};
	Dashboards.$name="Dashboards";
	Dashboards.provider="service";	
	Dashboards.$inject=["DashboardItems"];
	return Dashboards;
});