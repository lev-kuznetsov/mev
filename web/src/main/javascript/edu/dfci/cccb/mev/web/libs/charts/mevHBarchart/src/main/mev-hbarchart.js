define(["mui", "jquery", "d3", "nvd3", "angular-nvd3", "lodash", "mev-chart-utils",
    "./view/mevHBarchartDirective",
    "./services/mevBarchartNvd3Adaptor",
    "nvd3/build/nv.d3.css",
    "./style/mevHBarchart.less"], function(ng){
    return ng.module("mevHBarchart", arguments, arguments);
});