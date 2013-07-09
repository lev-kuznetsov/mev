'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
  controller('MyCtrl1', [function() {

  }])
  .controller('MyCtrl2', [function() {

  }])
  .controller('controllerUpload', [function($scope){
    //Function to upload files
    $scope.sendFile = function(){
      //Assign upload html elements to javascript variables. Used to track file upload progress.
      var bar = $("#bar");
      var percent = $("#percent");
      var output = $("#output");
      $(document).ready(function() 
      {
        var options = {
          //HTML element to receive server response
          target: "#output",
          //Tracks file upload progress
          uploadProgress: function(event, position, total, percentComplete){
            var percentVal = percentComplete + '%';
            bar.width(percentVal); //Grow the bar as file progress continues. Expressed as a percent.
            percent.html(percentVal); //Update the text that indicates what percent the upload progress is at. Percent expressed as text.
            output.html("Uploading..."); //Indicates to user that file upload is in progress.
          },
          success: function(){
            //HTML status upon successful file upload.
            var percentVal = "100%";
            bar.width(percentVal);
            percent.html(percentVal);
          }
        };
        //Prep form and pass options to jquery plugin to get ready for submission.  
        $('#myform').ajaxForm(options);
      });
      //Submit the form.  
      $('myform').submit(function()
      {
        $(this).ajaxSubmit();
        return false; //This command is used to prevent any page navigation during form submission to ensure ajax experience.
      });	
    };
  }]);