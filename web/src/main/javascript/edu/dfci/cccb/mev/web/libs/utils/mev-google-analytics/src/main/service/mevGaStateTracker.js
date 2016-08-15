define([], function(){
   var service = function(){
       return function(ga, toState, toParams, fromState, fromParams) {
           if (ga) {
               var page = toState.name.split(".").join("/").replace("root", "");
               if (toParams && toParams.analysisType)
                   page += "/" + toParams.analysisType;
               ga('set', 'page', page);
               ga('send', 'pageview');
           }
       };
   };
    service.$inject=[];
    service.$provider="service";
    service.$name="mevGaStateTracker";
    return service;
});