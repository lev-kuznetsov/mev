define(["mui", "mev-dotplot"], function(ng){"use strict";
	var app = ng.module("mev-dotplot-demo", arguments, arguments);

	app.controller('MainCtrl', function($scope) {
	  $scope.options = {
	            chart: {
	                type: 'multiBarHorizontalChart',
	                height: 450,
	                x: function(d){return d.label;},
	                y: function(d){return d.value;},
	                showControls: true,
	                showValues: true,
	                duration: 500,
	                xAxis: {
	                    showMaxMin: false
	                },
	                yAxis: {
	                    axisLabel: 'Values',
	                    tickFormat: function(d){
	                        return d3.format(',.2f')(d);
	                    }
	                },
	                barColor: function(d){
	                  return "pink";
	                }
	            }
	        };

	        $scope.data = [
	            {
	                "key": "Series2",
	                "color": "#1f77b4",
	                "values": [
	                    {
	                        "label" : "Group A" ,
	                        "value" : 25.307646510375
	                    } ,
	                    {
	                        "label" : "Group B" ,
	                        "value" : 16.756779544553
	                    } ,
	                    {
	                        "label" : "Group C" ,
	                        "value" : 18.451534877007
	                    } ,
	                    {
	                        "label" : "Group D" ,
	                        "value" : 8.6142352811805
	                    } ,
	                    {
	                        "label" : "Group E" ,
	                        "value" : 7.8082472075876
	                    } ,
	                    {
	                        "label" : "Group F" ,
	                        "value" : 5.259101026956
	                    } ,
	                    {
	                        "label" : "Group G" ,
	                        "value" : 0.30947953487127
	                    } ,
	                    {
	                        "label" : "Group H" ,
	                        "value" : 0
	                    } ,
	                    {
	                        "label" : "Group I" ,
	                        "value" : 0
	                    }
	                ]
	            }
	        ]
});


	ng.element(document).ready(function(){
		ng.bootstrap(document, [app.name]);
	});
});