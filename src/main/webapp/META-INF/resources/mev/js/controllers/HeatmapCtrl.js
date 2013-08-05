ctrl.controller('HeatmapCtrl', ['$scope', '$routeParams', '$http', function($scope, $routeParams, $http) {

	$scope.matrixlocation = $routeParams.matrixLocation;
	
	//Initial call for values
	$http({
		method:"GET",
		url:"heatmap/"+$scope.matrixlocation+"/data",
		params: {
			format:"json"
		}
	})
	.success( function(data) {
		$scope.heatmapcells = data;
	});

	$http({
		method:"GET",
		url:"heatmap/"+$scope.matrixlocation+"/annotation/dimension",
		params: {
			format:"json",
			dimension:"column"
		}
	})
	.success( function(data) {
		$scope.heatmapcolumns = data;
	});


	$http({
		method:"GET",
		url:"heatmap/"+$scope.matrixlocation+"/annotation/dimension",
		params: {
			format:"json",
			dimension:"row"
		}
	})
	.success( function(data) {
		$scope.heatmaprows = data;
	});
	
	//pull page function
	$scope.pullPage = function(startrow, endrow, startcol, endcol) {

		if (!scope.matrixlocation) {
			return;
		}

		$http({
			method:"GET",
			url:"heatmap/"+$scope.matrixlocation+"/data",
			params: {
				format:"json",
				startRow:startrow,
				endRow:endrow,
				startColumn:startcol,
				endColumn:endcol
			}
		})
		.success( function(data) {
			$scope.heatmapcells = data;
		});

		$scope.heatmapcolumns = [];
		for (var eachcol=startcol, eachcol<endcol, eachcol++) {
			$http({
				method:"GET",
				url:"heatmap/"+$scope.matrixlocation+"/annotation/dimension",
				params: {
					format:"json",
					dimension:"column",
					index:eachcol
				}
			})
			.success( function(data) {
				$scope.heatmapcolumns.push(data);
			});
		}

		$scope.heatmaprows = [];
		for (var eachrow=startrow, eachrow<endrow, eachrow++) {
			$http({
				method:"GET",
				url:"heatmap/"+$scope.matrixlocation+"/annotation/dimension",
				params: {
					format:"json",
					dimension:"row",
					index:eachrow
				}
			})
			.success( function(data) {
				$scope.heatmaprows.push(data);
			});
		}

		
	};

}]);


