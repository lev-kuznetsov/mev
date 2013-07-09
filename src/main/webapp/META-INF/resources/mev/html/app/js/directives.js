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
        inputData: "@",
        cellWidth: "@",
        cellHeight: "@",
        cellPadding: "@"
      },
      template: "<div><p>{{inputData.title}}</p></div>",
      link: function (scope, element, attrs) {
		  
        attrs.$observe('inputData', function(dataset) {
          
          //Debug flag for printing variables to javascript console
          var debug = true;
          var cellParams = {
            width: scope.cellWidth,
            height: scope.cellHeight,
            padding: scope.cellPadding
          };
          
          var visParams = {
            width : 0,
            height : 0,
            horizontal_padding : 30,
            vertical_padding : 10
          };

          //Probably need to update this to have a constant visualization
          //window size
          vis_params.width = (dataset.columns * (cell_params.width+cell_params.padding)) + (vis_params.vertical_padding*2);
          vis_params.height = (dataset.rows * (cell_params.height+cell_params.padding)) + (vis_params.horizontal_padding*2);

          
          if (debug) {
            // Debug Flag for Input Data
            console.log("Dataset: ");
            console.log(dataset);
            console.log(typeof dataset.columns)
            console.log(typeof dataset.rows)
          }

          if (debug) {
            // Debug Flag for Cell Params Values
            console.log("Cell Parameters:")
            console.log(cellParams)
            console.log("Cell Width: " + cellParams.width + " (" + typeof cell_params.width + ")");
            console.log("Cell Height: " + cellParams.height + " (" + typeof cell_params.height + ")");
          }
          
          if (debug) {
            // Debug flag for Vis Params 
            console.log("Vis_Params:")
            console.log(vis_params)
            console.log("Width: " + vis_params.width + " (" + typeof vis_params.width + ")");
            console.log("Height: " + vis_params.height + " (" + typeof vis_params.height + ")");
            console.log("HR Padding: " + vis_params.horizontal_padding + " (" + typeof vis_params.horizontal_padding + ")");
            console.log("VR Padding: " + vis_params.vertical_padding + " (" + typeof vis_params.vertical_padding + ")");
          }
          
          //Visualization Generation
          
          var svg = d3.select(element[0])
                      .append("svg")
                      .attr("width", vis_params.width)
                      .attr("height", vis_params.height);
						
          svg.selectAll("rect")
             .data(dataset.data)
             .enter()
             .append("rect")
             .attr("height", cell_params.height)
             .attr("width", cell_params.width)
             .attr("x", function(d, i) {
	           return xCellScale(i,dataset.columns)
             })
             .attr("y", function(d, i) {
	           return yCellScale(i,dataset.columns);
             })
             .attr("fill", function(d) {
               return "rgb(" + "0" + "," + colorCellScale(d) + ", 0 )"
             });

           var xCellScale = function(index, cols) {
               var output = (index%cols)*(cell_params.width+cell_params.padding);
               return output;
            }

            var yCellScale = function(index, cols) {
              var output = Math.floor(index/cols) * (cell_params.height+cell_params.padding);
              return output;
            }

            var colorCellScale = function(j) {	 
              var output = d3.scale.pow()
                           .domain([d3.min(dataset.data), d3.max(dataset.data)])
                           .rangeRound([0, 255]);
              return output(j);
            }

        });
		  
      }
    }
  }]);
