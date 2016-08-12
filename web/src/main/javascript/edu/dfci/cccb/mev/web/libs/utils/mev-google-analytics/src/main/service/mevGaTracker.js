define([], function(){
    var service = function($state, $stateParams){

        var _self = this;
        _self.pageView = function(page) {
            if (ga && page) {
                var pageRoot = $state.$current.name.split(".").join("/").replace("root", "");
                if ($stateParams && $stateParams.analysisType)
                    pageRoot += "/" + $stateParams.analysisType;
                if(!_.startsWith(page, "/"))
                    pageRoot += "/";
                ga('set', 'page', pageRoot + page);
                ga('send', 'pageview');
            }
        };
        _self.event = function(event){
            if(ga && event){
                ga('send', 'event', event.category, event.action, event.label, event.value);
            }
        }
    };
    service.$inject=["$state", "$stateParams"];
    service.$provider="service";
    service.$name="mevGaTracker";
    return service;
});