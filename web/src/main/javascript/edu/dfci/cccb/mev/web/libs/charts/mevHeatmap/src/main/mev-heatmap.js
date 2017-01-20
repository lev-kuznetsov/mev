define(["mui", "mev-bs-modal", "mev-chart-utils",
	"./view/heatmapvisualization/HeatmapVisualization",
	"./view/colorBrewer/mevColorBrewer",
	"./view/alertService/mevHeatmapAlert",
	"./view/heatmapvisualization/style/mev-heatmap.less"
	], function(ng){
	return ng.module("mev-heatmap", arguments, arguments);
});