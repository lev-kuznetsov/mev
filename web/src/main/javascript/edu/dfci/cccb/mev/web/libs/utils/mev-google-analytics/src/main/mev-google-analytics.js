define(["mui",
    "./router/mevGaRun",
    "./service/mevGaStateTracker.js",
    "./service/mevGaPageTracker.js",
    "./service/mevGaTracker.js"
], function(ng){
    return ng.module("mev-google-analytics", arguments, arguments);
});