define(["ng"], function(ng){
	var LayoutSrv = function LayoutSrv(){
		var columns={};
		this.register=function(column){
			columns[column.position]=column;
		};
		this.isClosed=function(position){
			return columns[position].isClosed();
		};
		this.toggle=function(position){
			columns[position].toggle();
		};
	};
	LayoutSrv.$inject=[];
	return LayoutSrv;
});
