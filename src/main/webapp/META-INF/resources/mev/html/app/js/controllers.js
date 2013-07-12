'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('MyCtrl1', [function() {

  }])
  .controller('MyCtrl2', [function() {

  }])
  .controller('controllerUpload', ['$scope', function($scope){
		$scope.output = "Select your file to upload.";
    //Function to upload files
    $scope.sendFile = function(){
			var input = document.getElementById("file");
			var bar = document.getElementById("bar");
			var percent = document.getElementById("percent");
			var upfile = input.files[0];
			var formdata = new FormData;
			formdata.append('upload', upfile);
			$scope.output = "Uploading...";
			
			var xhr = new XMLHttpRequest();
			xhr.upload.addEventListener("progress", function(e){
				var pc = parseInt((e.loaded / e.total * 100));
				bar.style.width = pc + "%";
				percent.innerHTML = pc + "%";
			});
			xhr.onreadystatechange = function(){
				if (xhr.readyState == 4 && xhr.status == 200){
					$scope.$apply(function(){
						$scope.output = xhr.responseText;
					});
					bar.style.width = "100%";
					percent.innerHTML = "100%";
				};
			};
			xhr.open('POST', 'http://bioed.bu.edu/cgi-bin/students_13/correiak/upload.py', true);
			xhr.send(formdata);
    };
  }]);