define(["mui", "mev-boxplot", "mev-chart-utils", "./boxplot_data.json"], function(ng, mevBoxplot, mevChartUtils, dataJson){
    var demo = ng.module("mev-boxplot-demo", arguments, arguments)
        .controller("demoCtrl", ["$scope", "$q", function($scope, $q){
            $scope.mockData = $q.when(dataJson);
        }]);

    ng.element(document).ready(function () {
        ng.bootstrap(document, [demo.name]);
    });
    return demo;
});