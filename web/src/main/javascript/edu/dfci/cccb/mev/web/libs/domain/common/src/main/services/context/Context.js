"use strict";
define(["lodash"], function(_){
	function ngcomponent($state){
		_.extend(this, {
			root: function(){
				return $state.$current.locals.globals.project;
			},
			current: function(){
				return $state.$current.path[$state.$current.path.length-1].locals.globals.analysis ||
				$state.$current.path[$state.$current.path.length-1].locals.globals.dataset ||
				$state.$current.path[$state.$current.path.length-1].locals.globals.project;
			}
		});					
	}
	ngcomponent.$inject=["$state"];
	ngcomponent.$name="mevContext";
	ngcomponent.$provider="service";
	return ngcomponent;	
});