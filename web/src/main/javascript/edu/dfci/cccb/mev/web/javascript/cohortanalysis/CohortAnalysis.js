define(['d3', 'angular'], function(d3, angular){
	
	angular.module('Mev.CohortAnalysis', [])
    .service('d3Service', function () {
    return d3
})
    .service('histogramExtractor', ['d3Service', function (d3) {
    // histogramExtractor [Numbers] -> Object
    //    Function to extract values for histogram processing
    return function (data) {

        return {
            'data': data,
                'min': d3.min(data),
                'max': d3.max(data)
        };
    }
}])
.directive('cohortDashboard', [ function () {
	
	var pieChartTemplate = '<d3-Pie-Chart data="viewModel" height="300" width="500"></d3-Pie-Chart>';
    var histogramTemplate = '<d3-Histogram data="viewModel" height="300" width="500"></d3-Histogram>';
    
    var getTemplate = function(type) {
        var template = '';

        switch(type) {
            case 'Hierarchical Clustering':
                template = histogramTemplate;
                break;
            case 'K-means Clustering':
                template = pieChartTemplate;
                break;
        }

        return template;
    };
	
    return {
        'restrict': 'E',
            'scope': {
            	'viewModel': '=data',
            	'viewType': '=type'
            },
            'link': function(scope, element, attrs) {

            		scope.$watch('viewModel', function(newval){
                        element.append(getTemplate(scope.viewModel));
                        
                        $compile(element.contents())(scope);
            		});

            }
    };
    
}])
    .directive('d3Histogram', ['histogramExtractor', 'd3Service', function (histogramExtractor, d3) {
    return {
        'restrict': 'E',
            'scope': {
            'histData': '=data',
                'height': '@height',
                'width': '@width',
        },
            'link': function (scope, elems, attrs) {

            var formatCount = d3.format(",.0f");

            var margin = {
                top: 10,
                right: 30,
                bottom: 30,
                left: 30
            },
            width = scope.width - margin.left - margin.right,
                height = scope.height - margin.top - margin.bottom;

            d3.select(elems[0]).append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom)
                .attr('class', 'histogram')

            var svg = d3.select(elems[0]).select("svg.histogram")

            svg.append("g")
            .attr('class','visualization')
            .attr("transform", 
                "translate(" + margin.left + "," + margin.top + ")");

            var viz = svg.select("g.visualization");

            scope.$watch('histData', function (newval, oldval) {

                if (newval) {

                    viz.selectAll("*").remove()

                    var x = d3.scale.linear()
                        .domain([histData.min,histData.max])
                        .range([0, width]);

                    var data = d3.layout.histogram()
                      .bins(x.ticks(20))
                    (histData.data);

                    var y = d3.scale.linear()
                        .domain([0, d3.max(data, function (d) {
                        return d.y;
                    })])
                        .range([height, margin.top]);

                    var xAxis = d3.svg.axis()
                        .scale(x)
                        .orient("bottom");

                    var bar = viz.selectAll(".bar")
                        .data(data)
                        .enter().append("g")
                        .attr("class", "bar")
                        .attr("transform", function (d) {
                        return "translate(" + x(d.x) + "," + y(d.y) + ")";
                    });

                    bar.append("rect")
                        .attr("x", 1)
                        .attr("width", x(data[0].dx) - 1)
                        .attr("height", function (d) {
                        return height - y(d.y);
                    });

                    bar.append("text")
                        .attr("dy", ".75em")
                        .attr("y", 6)
                        .attr("x", x(data[0].dx) / 2)
                        .attr("text-anchor", "middle")
                        .text(function (d) {
                        return formatCount(d.y);
                    });

                    viz.append("g")
                        .attr("class", "x axis")
                        .attr("transform", "translate(0," + height + ")")
                        .call(xAxis);

                    //End of if statement    
                };


                //End of scope.data watcher  
            });



            //End of link function    
        },


        //End of return object
    };
}])
.service('PieChartExtractor', [function(){
	return function(data){
		return data
	}
}])
.directive('d3PieChart', ['d3Service', 'PieChartExtractor', function(d3, PCExtract){
    return {
        'restrict': 'E',
        'scope' : {
            'data' : '=data',
            'width' : '@width',
            'height' : '@height',
            'on': '@dimension'
        },
        'link': function(scope, elems, attrs){
            
            var width = scope.width,
                height = scope.height,
                radius = Math.min(scope.width, scope.height) / 2;
            
            var color = d3.scale.ordinal()
                .range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);
            
            var arc = d3.svg.arc()
                .outerRadius(radius - 10)
                .innerRadius(0);
            
            var pie = d3.layout.pie()
                .sort(null)
                .value(function(d) { return d.population; });
            
            d3.select(elems[0]).append("svg")
                .attr("width", width)
                .attr("height", height);
            
            var svg = d3.select(elems[0]).select('svg');
            
            svg.append("g")
                .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
            var viz = svg.select('g');
            
            scope.$watch("data", function(newval, oldval) {
            
                if (newval){
                    
                	  extract = PCExtract(data);
                	  
                      extract.forEach(function(d) {
                        d.population = parseFloat(d.population);
                      });
                    
                      var g = viz.selectAll(".arc")
                          .data(pie(scope.data))
                        .enter().append("g")
                          .attr("class", "arc");
                    
                      g.append("path")
                          .attr("d", arc)
                          .style("fill", function(d) { 
                              return color(d[scope.on]); 
                          });
                    
                      g.append("text")
                          .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
                          .attr("dy", ".35em")
                          .style("text-anchor", "middle")
                          .text(function(d) { return d.data.age; });
                 };
              
            
            });
            
        //End link function
        }
    //End restrict object
    }
    
//End of directive
}]);
	
});

