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
        inputcolor:"=",
        inputsize:"="
      },
    //  template: '<div>{{inputdata}}</div>',
      link: function (scope, element, attrs) {
		
		
		//Variable Declarations
		var debug = true;
		
		scope.visParams = {
            width : 600,
            height : 900,
            horizontal_padding : 30,
            vertical_padding : 10
        };
        
        scope.cellParams = {
            width: 0,
            height: 0,
            padding: 3
        };
		
		var svg = d3.select(element[0])
                      .append("svg")
                      .attr("width", scope.visParams.width )
                      .attr("height", scope.visParams.height);
		
		var xCellScale = function(index, cols, cellPs) {
            return (index%cols)*(cellPs.width+(cellPs.padding/2));         
          }
          
        var yCellScale = function(index, cols, cellPs) {
            return Math.floor(index/cols) * (cellPs.height+cellPs.padding);
        };

        var colorCellScale = function(j) {	 
            var output = d3.scale.linear()
                           .domain([d3.min(scope.inputdata.data), d3.max(scope.inputdata.data)])
                           .rangeRound([0, 255]);
            return output(j);
        };
        
        var buildVisualization = function () {
		
		    svg.selectAll("rect")
               .data(scope.inputdata.data)
               .enter()
               .append("rect")
               .attr("height", scope.cellParams.height)
               .attr("width", scope.cellParams.width)
               .attr("x", function(d, i) {
                 return xCellScale(i, scope.inputdata.columns, scope.cellParams);;
               })
               .attr("y", function(d, i) {
	             return yCellScale(i, scope.inputdata.rows, scope.cellParams);
               })
               .attr("fill", function(d) {
                 return "rgb(" + "0" + "," + colorCellScale(d) + ", 0 )"
               });	
			
        }
        
        scope.$watch('inputdata', function(foo) {
        //Builds visualization on inputdata insert
          
          scope.cellParams.width = ((scope.inputsize + 1) /3) *Math.floor( (scope.visParams.width / scope.inputdata.columns) - scope.cellParams.padding );
          scope.cellParams.height = scope.cellParams.width;
          
          if (debug) {
            // Debug Flag for Input Data
            
            console.log("Input Data Change ======================================");
            console.log("Input Data: ");
            console.log(scope.inputdata);
            console.log("Columns: " + scope.inputdata.columns + " (" + typeof scope.inputdata.columns);
            console.log("Rows: " + scope.inputdata.rows + " (" + typeof scope.inputdata.rows);

            // Debug Flag for Cell Params Values
            console.log("Cell Parameters:");
            console.log(scope.cellParams);
            console.log("Cell Width: " + scope.cellParams.width + " (" + typeof scope.cellParams.width + ")");
            console.log("Cell Height: " + scope.cellParams.height + " (" + typeof scope.cellParams.height + ")");

            // Debug flag for Vis Params 
            console.log("visParams:");
            console.log(scope.visParams);
            console.log("Width: " + scope.visParams.width + " (" + typeof scope.visParams.width + ")");
            console.log("Height: " + scope.visParams.height + " (" + typeof scope.visParams.height + ")");
            console.log("HR Padding: " + scope.visParams.horizontal_padding + " (" + typeof scope.visParams.horizontal_padding + ")");
            console.log("VR Padding: " + scope.visParams.vertical_padding + " (" + typeof scope.visParams.vertical_padding + ")");
          }

          //Build Visualization from scratch
          
          buildVisualization();
          
        });
        
        scope.$watch('inputsize', function(newdata, olddata) {
			
		  scope.cellParams.width = ((scope.inputsize + 1) /3) *Math.floor( (scope.visParams.width / scope.inputdata.columns) - scope.cellParams.padding );
          scope.cellParams.height = scope.cellParams.width;
          
          if (debug) {
            // Debug Flag for Input Data
            
            console.log("Input Data Change ======================================");
            console.log("Input Data: ");
            console.log(scope.inputdata);
            console.log("Columns: " + scope.inputdata.columns + " (" + typeof scope.inputdata.columns);
            console.log("Rows: " + scope.inputdata.rows + " (" + typeof scope.inputdata.rows);

            // Debug Flag for Cell Params Values
            console.log("Cell Parameters:");
            console.log(scope.cellParams);
            console.log("Cell Width: " + scope.cellParams.width + " (" + typeof scope.cellParams.width + ")");
            console.log("Cell Height: " + scope.cellParams.height + " (" + typeof scope.cellParams.height + ")");

            // Debug flag for Vis Params 
            console.log("visParams:");
            console.log(scope.visParams);
            console.log("Width: " + scope.visParams.width + " (" + typeof scope.visParams.width + ")");
            console.log("Height: " + scope.visParams.height + " (" + typeof scope.visParams.height + ")");
            console.log("HR Padding: " + scope.visParams.horizontal_padding + " (" + typeof scope.visParams.horizontal_padding + ")");
            console.log("VR Padding: " + scope.visParams.vertical_padding + " (" + typeof scope.visParams.vertical_padding + ")");
          }
          
          //Rebuild Visualization with new sizes
          
          svg.selectAll("cell rect")
             .transition()
             .duration(500)
             .delay(function(d,i) {return (i%m)*10;})
             .attr("x", function(d, i) {
                 return xCellScale(i, scope.inputdata.columns, scope.cellParams);;
               })
               .attr("y", function(d, i) {
	             return yCellScale(i, scope.inputdata.rows, scope.cellParams);
               })
               .attr("fill", function(d) {
                 return "rgb(" + "0" + "," + colorCellScale(d) + ", 0 )"
               });	
          
        });
		  
      }
    }
  }]);
