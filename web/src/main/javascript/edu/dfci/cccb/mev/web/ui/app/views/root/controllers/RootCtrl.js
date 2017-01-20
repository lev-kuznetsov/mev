define([], function(){
	var RootCtrl = function(SideMenuSrv, $state, $scope, $rootScope, sidepanelSrvc){
		
		var header={
			fixed: true
		};
		var footer={
			fixed: true
		};
		var columnScroll=true;
		var columnContentScroll=true;
		var isLeftLayoutClosed=false;
		
		this.isSideMenuCollapse=function(){
//			console.debug("root.isShrink", SideMenuSrv.isShrink());
			return SideMenuSrv.isShrink();
		}
		this.isSideMenu=function(){
			var result = $state.current.data && $state.current.data.sidemenuUrl ? true : false;
//			console.debug("root.isSideMenu", $state.current.data, result);
			return result;
		};
		
		this.isHeaderFixed=function(){
			return header.fixed;
		};		
		$scope.$on("ui:toggleFixedHeader", function(){
			console.debug("ui:toggleFixedHeader", header.fixed);
			header.fixed=!header.fixed;
		});

		this.isFooterFixed=function(){
			return footer.fixed;
		};		
		$scope.$on("ui:toggleFixedFooter", function(){
			console.debug("ui:toggleFixedFooter", footer.fixed);
			footer.fixed=!footer.fixed;
		});
		
		this.toggleLeft=function(){
			isLeftLayoutClosed=!isLeftLayoutClosed;
			$rootScope.$broadcast("ui:layoutColumn:toggle", {position: "left"});			
			console.debug("hide left");
//			sidepanelSrvc.toggle("left");
		};
		this.isLeftClosed=function(){			
			return sidepanelSrvc.isCollapsed();
		};
		
		$scope.$on("ui:toggleColumnScroll", function(){
			console.debug("ui:toggleColumnScroll", columnScroll);
			columnScroll=!columnScroll;
		});
		this.isColumnScroll=function(){
			return columnScroll;
		};
		$scope.$on("ui:toggleColumnContentScroll", function(){
			console.debug("ui:toggleColumnContentScroll", columnContentScroll);
			columnContentScroll=!columnContentScroll;
		});
		this.isColumnContentScroll=function(){
			return columnContentScroll;
		};
		this.curDate=function(){
			return new Date();
		}
		
		this.getCss=function(){
			var css = {
				'fixed-header': this.isHeaderFixed(), 
				'fixed-footer': this.isFooterFixed()
			};
			$state.current.name.split(".").map(function(item){css["state-"+item]=true;});
			return css;
		}
	};	
	
	RootCtrl.$inject=["SideMenuSrv", "$state", "$scope", "$rootScope", "sidepanelSrvc"];
	RootCtrl.$name="RootCtrl";
	RootCtrl.$provider="controller";
	return RootCtrl;
});