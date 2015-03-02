define(["ng", "lodash"], function(ng, _){
	var counter=0;
	var ProjectViewVM = function($scope, $stateParams, $state, project){
		var that=this;
		var project=project;
		console.debug("projectViewVM: ", counter++, project);
		this.getProjectName=function(){
			return project.name;
		};
		this.getProject=function(){
			return project;
		};
		
		this.toggelColumn=function(){
			
		};
		
		this.node={nodeName: "Home"};
		
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
		
		this.options = {
			onNodeSelect: function (node, breadcrums) {
				$scope.breadcrums = breadcrums;
			}
		};
		
//		$scope.standardItems = [{ sizeX: 2, sizeY: 2, row: 0, col: 0 },
//		                        { sizeX: 2, sizeY: 2, row: 0, col: 2 },
//		                        { sizeX: 2, sizeY: 2, row: 0, col: 4 },
//		                        { sizeX: 6, sizeY: 6, row: 2, col: 0 }];
		$scope.standardItems = [{ sizeX: 1, sizeY: 1, row: 0, col: 0 },
		                        { sizeX: 1, sizeY: 1, row: 0, col: 1 },
		                        { sizeX: 1, sizeY: 1, row: 0, col: 2 },
		                        { sizeX: 3, sizeY: 3, row: 1, col: 0 }];
		$scope.gridsterOpts={
	        columns: 3, // the width of the grid, in columns
	        pushing: true, // whether to push other items out of the way on move or resize
	        floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
	        swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
	        width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
	        colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
	        rowHeight: 'match', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
	        margins: [10, 10], // the pixel distance between each widget
	        outerMargin: true, // whether margins apply to outer edges of the grid
	        isMobile: false, // stacks the grid items if true
	        mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
	        mobileModeEnabled: true, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
	        minColumns: 1, // the minimum columns the grid must have
	        minRows: 2, // the minimum height of the grid, in rows
	        maxRows: 100,
	        defaultSizeX: 2, // the default width of a gridster item, if not specifed
	        defaultSizeY: 1, // the default height of a gridster item, if not specified
	        minSizeX: 1, // minimum column width of an item
	        maxSizeX: null, // maximum column width of an item
	        minSizeY: 1, // minumum row height of an item
	        maxSizeY: null, // maximum row height of an item
	        resizable: {
	           enabled: true,
	           handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
	           start: function(event, $element, widget) {}, // optional callback fired when resize is started,
	           resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
	           stop: function(event, $element, widget) {} // optional callback fired when item is finished resizing
	        },
	        draggable: {
	           enabled: true, // whether dragging items is supported
	           handle: '.my-class', // optional selector for resize handle
	           start: function(event, $element, widget) {}, // optional callback fired when drag is started,
	           drag: function(event, $element, widget) {}, // optional callback fired when item is moved,
	           stop: function(event, $element, widget) {} // optional callback fired when item is finished dragging
	        }
	    };
		$scope.$on("ui:projectTree:nodeSelected", function($event, node){
			that.node=node;			
			
			var params = node.nodeConfig.state.getParams(node);
			if(node.nodeParent && node.nodeParent.nodeConfig){
				ng.extend(params, node.nodeParent.nodeConfig.state.getParams(node.nodeParent));
			}
			
			var targetState = "root.project"+node.nodeConfig.state.name;
			console.debug("ui:projectTree:nodeSelected $on", $event, node, $state, params, targetState);
			
			$state.go(targetState, params);
		});
	};
	
	ProjectViewVM.$inject = ["$scope", "$stateParams", "$state", "project"];
	return ProjectViewVM;
});