define(["mui",
   "./services/mevTooltipContent",
    "./model/mevChartDimConfig",
   "./model/mevChartColorDimConfig",
    "./view/colorLegend/mevChartColorLegendDirective",
    "./style/mevChart.less",
   "./style/mevTooltipContent.less"], function(ng){
   return ng.module("mev-chart-utils", arguments, arguments);
});
