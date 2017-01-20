define(["lodash"], function(_){ "use strict";
	function ngcomponent($state, $stateParams){
		_.extend(this, {
			root: function(){
				return $state.$current.locals.globals.project;
			},
			current: function(){
				return $state.$current.path[$state.$current.path.length-1].locals.globals.analysis ||
				$state.$current.path[$state.$current.path.length-1].locals.globals.dataset ||
				$state.$current.path[$state.$current.path.length-1].locals.globals.project;
			},
			setLevel: function(level){				
				this.level = level || "root";
			},
			getLevel: function(){
				return this.level || "root";
			},
			get: function(level){
				var root = this.root();
				if(level === "root")
					return root;
				else if(level === "dataset"){
					return root
						? root.dataset
						: undefined;
				}else
					return this.current();
			}
		});					
	}
	ngcomponent.$inject=["$state", "$stateParams"];
	ngcomponent.$name="mevContext";
	ngcomponent.$provider="service";
	return ngcomponent;	
});