'use strict'

/* Controllers */

angular.module('myApp.controllers', [])
  .controller('HeatmapCtrl', ['$scope', '$routeParams', '$http', function($scope, $routeParams, $http) {
    
    //Import Matrix Location Routing Parameter
    $scope.matrixLocation = $routeParams.matrixLocation;
    
    //Watch a change in route parameter?
    
      //Send HTTP Request to import data
      
    $http.get('data/' + $scope.matrixLocation + '.json').
    
      success(function (data) {
        $scope.dataset = data;
        $scope.colorscheme = 0;
        $scope.cellsize = 1;
	  });
    
      //Somehow handle http request failure
    $scope.setColorScheme = function(colorcode) {
		$scope.colorscheme = colorcode;
	}
	
	$scope.setCellSize = function(sizecode) {
		$scope.cellsize = sizecode;
	}

  }])
  .controller('AnalyzeCtrl', ['$scope', '$http', function($scope, $http) {
    
    //Get visualization json
    $http.get('data/visualization_data.json').
    
	success(function (data) {
        $scope.visualizationdata = data;
	});
	  
	$http.get('data/upload_data.json').
		  success(function (data) {
			  $scope.uploaddata = data;
		  });
      
	}])
  .controller('UploadCtrl', ['$scope', function($scope){
		$scope.output = "Select your file to upload.";
        //Function to upload files
        $scope.sendFile = function() {
			//Variable declarations
			var input = document.getElementById("file");
			var bar = document.getElementById("bar"); //DOM element of the growing progress bar
			var percent = document.getElementById("percent"); //Value reported inside progress bar
			var upfile = input.files[0]; //Assign file object of the input file to a variable
			var formdata = new FormData; //Dynamically create a new form
			formdata.append('upload', upfile); //Append file object to the form
			$scope.output = "Uploading..."; //Alert user that uploading has begun
			//AJAX code
			var xhr = new XMLHttpRequest();
			//Event Listener that will provide progress to the user
			xhr.upload.addEventListener("progress", function(e){
				var pc = parseInt((e.loaded / e.total * 100)); //Calculate % completion
				//Update the progress elements with the percent completion quantity.
				bar.style.width = pc + "%";
				percent.innerHTML = pc + "%";
			});
			//Event Listener that will output the server response (if any) as well as update the user feedback elements to 100% completion.
			xhr.onreadystatechange = function(){
				if (xhr.readyState == 4 && xhr.status == 200){
					$scope.$apply(function(){ //$scope.$apply() is used so angular controller can update $scope.output upon the xhr.responseText assignment.
						$scope.output = xhr.responseText;
					});
					bar.style.width = "100%";
					percent.innerHTML = "100%";
				};
			};
			//Send the uploaded file.
			xhr.open('POST', 'http://bioed.bu.edu/cgi-bin/students_13/correiak/upload.py', true); //Action (2nd parameter) is currently set to my school webspace for testing purposes. To be changed.
			xhr.send(formdata);
      };
  }])
  .controller('GeneSelectCtrl', ['$scope', '$http', '$routeParams', function($scope, $http, $routeParams) {
    
    //Variables
    $scope.dataset = $routeParams.dataset;
    $scope.currentPage = 1;
    $scope.genesCurrentPage = [];
    $scope.nearbyPages = [];
    $scope.genesSearched = [];
    $scope.genesMarked = [];
    $scope.geneQuery = [];
    $scope.pageMax = [];
    
    //Functions
    $scope.pageUp = function() {
        if ($scope.currentPage < $scope.pageMax) {
           $scope.getPage($scope.currentPage + 1);
        }
    };
    $scope.pageDown = function() {
        if ($scope.currentPage < 1) {
           $scope.getPage($scope.currentPage-1);
        }
    };
    $scope.pageDown = function() {

    };
    $scope.searchGene = function(query) {
        $http.get('data/' + $scope.dataset + 'q=' + query)
             .success(function(data) {
                 $scope.genesSearched = data;
             });
    });

    $scope.getPage = function(pageId) {
        $http.get('data/' + $scope.dataset + '-' + pageId)
             .success(function(data) {
               $scope.genesCurrentPage = data.genes;
               $scope.maxPage = data.maxPage;
               $scope.nearbyPages = data.nearbyPages;
               $scope.currentPage = pageId;
             });
    };

    $scope.addMark = function(geneMark) {
        $http.post('data/' + $scope.dataset + '/marks/' geneMark);

    });

    $scope.getMarks = function() {
        $http.get('data/' + $scope.dataset + '/marks')
             .success(function(data) {
               $scope.genesMarked = data;
             });
    });
    //http request for current page of genes (20 max) with listener for gene add click
    $scope.getPage(1);

  }]);
