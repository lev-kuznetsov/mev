define([], function(){
    var run = function($rootScope, $window, mevGaStateTracker){
        $rootScope.$on('$stateChangeSuccess',
            function(event, toState, toParams, fromState, fromParams){
                if($window.ga){
                    mevGaStateTracker($window.ga, toState, toParams, fromState, fromParams);
                }
            });
    };
    run.$inject=["$rootScope", "$window", "mevGaStateTracker"];
    run.$provider="run";
    return run;
});