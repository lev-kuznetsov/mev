define(["lodash"], function(_){
   var service = function($state, $stateParams){
       return function(page) {
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
   };
    service.$inject=["$state"];
    service.$provider="service";
    service.$name="mevGaPageTracker";
    return service;
});