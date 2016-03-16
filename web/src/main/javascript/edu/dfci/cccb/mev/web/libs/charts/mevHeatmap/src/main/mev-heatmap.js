define(["mui", "mev-bs-modal",
	"./view/heatmapvisualization/HeatmapVisualization",
	"./view/colorBrewer/mevColorBrewer",
	"./view/alertService/mevHeatmapAlert",
	"./style/mev-heatmap.less"
	], function(ng){
	return ng.module("mev-heatmap", arguments, arguments);
});