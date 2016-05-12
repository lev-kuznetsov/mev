define(["mui", 
   "./tooltip/services/mevTooltipContent",
    "./dimConfig/model/mevChartConfig",
    "./dimConfig/model/mevChartDimConfig",
   "./dimConfig/model/mevChartColorDimConfig",
    "./colorLegend/view/mevChartColorLegendDirective",
    "./colorLegend/view/mevGlyphAltDirective",
    "./saveAs/svgSaveAsPng",
    "./saveAs/svgSaveAsDirective",
    "./style/mevChart.less",
   "./tooltip/style/mevTooltipContent.less"], function(ng){
   return ng.module("mev-chart-utils", arguments, arguments);
});
