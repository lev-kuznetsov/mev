define([], function(){
	var AngularTreeviewMixin = function AngularTreeviewMixin(treeViewDefaults){
		this.tree = {
			folders : [ {
				name : 'Folder 1',
				files : [ {
					name : 'File 1.jpg'
				}, {
					name : 'File 2.png'
				} ],
				folders : [ {
					name : 'Subfolder 1',
					files : [ {
						name : 'Subfile 1'
					} ]
				}, {
					name : 'Subfolder 2'
				}, {
					name : 'Subfolder 3'
				} ]
			}, {
				name : 'Folder 2'
			} ]
		};
		this.options=treeViewDefaults;
	}	
	AngularTreeviewMixin.$inject=["treeViewDefaults"];
});
