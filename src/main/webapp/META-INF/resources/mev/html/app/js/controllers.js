'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('MyCtrl1', [function() {

  }])
  .controller('MyCtrl2', [function() {

  }])
  .controller('controllerUpload', ['$scope', function($scope){
		//Array of input dialogues 
		$scope.inputs = [];
    //Function to upload files
    $scope.sendFile = function(i){
			var len = document.getElementsByClassName("fileup").length;
			var input = document.getElementById("file" + i);
			var bar = document.getElementById("b" + i);
			var percent = document.getElementById("p" + i);
			
			if (i >= len){
				return;
			}
			else if (!(input.value)){
				i++;
				$scope.sendFile(i);
			}
			else{
				var upfile = input.files[0];
				var formdata = new FormData;
				formdata.append('upload', upfile);
				
				var xhr = new XMLHttpRequest();
				xhr.upload.addEventListener("progress", function(e){
					var pc = parseInt((e.loaded / e.total * 100));
					bar.style.width = pc + "%";
					percent.innerHTML = pc + "%";
				});
				xhr.onreadystatechange = function(){
					if (xhr.readyState == 4 && xhr.status == 200){
						document.getElementById("output").innerHTML = xhr.responseText;
						bar.style.width = "100%";
						percent.innerHTML = "100%";
						i++;
						$scope.sendFile(i);
					};
				};
				xhr.open('POST', 'http://bioed.bu.edu/cgi-bin/students_13/correiak/upload.py', true);
				xhr.send(formdata);
			};
      // //Assign upload html elements to javascript variables. Used to track file upload progress.
      // var bar = $("#bar");
      // var percent = $("#percent");
      // var output = $("#output");
      // $(document).ready(function() {
        // var options = {
          // //HTML element to receive server response
          // target: "#output",
          // //Tracks file upload progress
          // uploadProgress: function(event, position, total, percentComplete){
            // var percentVal = percentComplete + '%';
            // bar.width(percentVal); //Grow the bar as file progress continues. Expressed as a percent.
            // percent.html(percentVal); //Update the text that indicates what percent the upload progress is at. Percent expressed as text.
            // output.html("Uploading..."); //Indicates to user that file upload is in progress.
          // },
          // success: function(){
            // //HTML status upon successful file upload.
            // var percentVal = "100%";
            // bar.width(percentVal);
            // percent.html(percentVal);
          // }
        // };
        // //Prep form and pass options to jquery plugin to get ready for submission.  
        // $('#myform').ajaxForm(options);
      // });
      // //Submit the form.  
      // $('myform').submit(function()
      // {
        // $(this).ajaxSubmit();
        // return false; //This command is used to prevent any page navigation during form submission to ensure ajax experience.
      // });	
    };
		$scope.addInput = function(){
			var i = document.getElementsByClassName("fileup").length;
			var inputhtml = '<div class="fileup">' +
												'<input id="file' + i + '" name="upload" type="file" />' +
												'<div class="progressbar">' +
													'<div class="bar" id="b' + i + '">' +
													'</div><div class="percent" id="p' + i + '">0%</div>' +
												'</div>' +
											'</div>';
			$scope.inputs.push({html:inputhtml});
		};
  }]);