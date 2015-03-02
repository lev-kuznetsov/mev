define([], function(){
	var IssuesVM = function($scope){
		$scope.gridsterOpts = {
				margins: [10, 10],
				outerMargin: true,
				pushing: true,
				floating: true,
				draggable: {
					enabled: true
				},
				resizable: {
					enabled: true,
					handles: ['n', 'e', 's', 'w', 'se', 'sw']
				}
			};
		
		$scope.standardItems = [
		                        { sizeX: 2, sizeY: 1, row: 0, col: 0 },
		                        { sizeX: 2, sizeY: 2, row: 0, col: 2 },
		                        { sizeX: 1, sizeY: 1, row: 0, col: 4 },
		                        { sizeX: 1, sizeY: 1, row: 0, col: 5 },
		                        { sizeX: 2, sizeY: 1, row: 1, col: 0 },
		                        { sizeX: 1, sizeY: 1, row: 1, col: 4 },
		                        { sizeX: 1, sizeY: 2, row: 1, col: 5 },
		                        { sizeX: 1, sizeY: 1, row: 2, col: 0 },
		                        { sizeX: 2, sizeY: 1, row: 2, col: 1 },
		                        { sizeX: 1, sizeY: 1, row: 2, col: 3 },
		                        { sizeX: 1, sizeY: 1, row: 2, col: 4 }
		                      ];
		
		
		$scope.outerGridster = {
		    config: {
//		    	mobileModeEnabled: false,
		        columns: 4,
		        margins: [5,5]
		    },
		    items: [
		        { sizeX: 2, sizeY: 1, row: 0, col: 0 },
		        { sizeX: 2, sizeY: 1, row: 0, col: 2 },
		        { sizeX: 2, sizeY: 1, row: 1, col: 0 },
		        { sizeX: 2, sizeY: 1, row: 1, col: 2 }
		    ]
		};

		// setup for inner gridster instance... 
		$scope.innerGridster = {
		   config: {
		        columns: 2,
		        mobileModeEnabled: false,		        
		        margins: [5,5]
		    },
		    items: [
		        { sizeX: 2, sizeY: 1, row: 0, col: 0 },
		        { sizeX: 2, sizeY: 1, row: 0, col: 2 }
		    ]
		};
		
	};
	IssuesVM.$inject=["$scope"];
	return IssuesVM;
});
