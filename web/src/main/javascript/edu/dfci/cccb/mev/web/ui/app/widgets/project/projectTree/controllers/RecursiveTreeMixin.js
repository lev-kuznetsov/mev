define([], function(){
	
	return function RecursiveTreeMixin() {
	    this.delete = function(data) {
	        data.nodes = [];
	    };
	    this.add = function(data) {
	        var post = data.nodes.length + 1;
	        var newName = data.name + '-' + post;
	        data.nodes.push({name: newName,nodes: []});
	    };
	    this.tree = [{name: "Bloat", nodes: []}];
	}
	
});