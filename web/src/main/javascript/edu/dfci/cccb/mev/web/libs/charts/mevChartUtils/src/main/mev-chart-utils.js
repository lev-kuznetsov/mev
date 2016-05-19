define(["mui", "mev-glyph-alt",
   "./tooltip/services/mevTooltipContent",
    "./dimConfig/model/mevChartConfig",
    "./dimConfig/model/mevChartDimConfig",
   "./dimConfig/model/mevChartColorDimConfig",
    "./colorLegend/view/mevChartColorLegendDirective",
    "./saveAs/svgSaveAsPng",
    "./saveAs/svgSaveAsDirective",
    "./style/mevChart.less",
   "./tooltip/style/mevTooltipContent.less"], function(ng){
   return ng.module("mev-chart-utils", arguments, arguments);
});
