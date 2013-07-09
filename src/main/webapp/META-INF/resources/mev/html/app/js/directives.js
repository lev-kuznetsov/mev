'use strict';

/* Directives */


angular.module('myApp.directives', []).
  directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }]).
  directive('visHeatmap', [function() {
  /* Heatmap visualization function.
  * 
  */
    return {
      restrict: 'E',
      scope: {
        inputdata:"=",
        inputrows:"=",
        inputcolumns:"="
      },
      template: '<div><p>{{inputdata}}</p></div>',
      link: function (scope, element, attrs) {
		  
        scope.$watch('inputdata', function(newdata, olddata) {
          
          //Debug flag for printing variables to javascript console
          var debug = true;
          var cellParams = {
            width: 20,
            height: 20,
            padding: 10
          };
          
          var visParams = {
            width : 0,
            height : 0,
            horizontal_padding : 30,
            vertical_padding : 10
          };

          //Probably need to update this to have a constant visualization
          //window size
          visParams.width = (scope.inputcolumns * (cellParams.width+cellParams.padding)) + (visParams.vertical_padding*2);
          visParams.height = (scope.inputrows * (cellParams.height+cellParams.padding)) + (visParams.horizontal_padding*2);

          
          if (debug) {
            // Debug Flag for Input Data
            console.log("Input Data: ");
            console.log(newdata);
            console.log(typeof scope.inputcolumns)
            console.log(typeof scope.inputrows)
          }

          if (debug) {
            // Debug Flag for Cell Params Values
            console.log("Cell Parameters:")
            console.log(cellParams)
            console.log("Cell Width: " + cellParams.width + " (" + typeof cellParams.width + ")");
            console.log("Cell Height: " + cellParams.height + " (" + typeof cellParams.height + ")");
          }
          
          if (debug) {
            // Debug flag for Vis Params 
            console.log("visParams:")
            console.log(visParams)
            console.log("Width: " + visParams.width + " (" + typeof visParams.width + ")");
            console.log("Height: " + visParams.height + " (" + typeof visParams.height + ")");
            console.log("HR Padding: " + visParams.horizontal_padding + " (" + typeof visParams.horizontal_padding + ")");
            console.log("VR Padding: " + visParams.vertical_padding + " (" + typeof visParams.vertical_padding + ")");
          }
          
          //Visualization Generation
          
          var svg = d3.select(element[0])
                      .append("svg")
                      .attr("width", visParams.width)
                      .attr("height", visParams.height);
						
          svg.selectAll("rect")
             .data(scope.inputdata)
             .enter()
             .append("rect")
             .attr("height", cellParams.height)
             .attr("width", cellParams.width)
             .attr("x", function(d, i) {
               var output = scope.xCellScale(i, scope.inputcolumns);
               return output;
             })
             .attr("y", function(d, i) {
	           return yCellScale(i,scope.inputcolumns);
             })
             .attr("fill", function(d) {
               return "rgb(" + "0" + "," + colorCellScale(d) + ", 0 )"
             });

           var xCellScale = function(index, cols) {
               var output = (index%cols)*(cellParams.width+cellParams.padding);
               return output;
            };

            var yCellScale = function(index, cols) {
              var output = Math.floor(index/cols) * (cellParams.height+cellParams.padding);
              return output;
            };

            var colorCellScale = function(j) {	 
              var output = d3.scale.pow()
                           .domain([d3.min(scope.inputdata.data), d3.max(scope.inputdata.data)])
                           .rangeRound([0, 255]);
              return output(j);
            };

        });
		  
      }
    }
  }]);
