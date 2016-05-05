define(["mui", "d3", "ng-vega", "./mynet.json"],
function(ng, d3, vg, networkJson){
    var demo = ng.module("mev-network-graph-demo", arguments, arguments)
        .controller("DemoCtrl", ["$scope", function($scope){
            $scope.network = networkJson;
            $scope.data = {
                "tooltipFields": [{"field": "name"},
                    {"field": "index" },
                    {"field": "group" }]
            }
        }]);
    ng.element(document).ready(function(){
        ng.bootstrap(document, [demo.name]);
    });
});