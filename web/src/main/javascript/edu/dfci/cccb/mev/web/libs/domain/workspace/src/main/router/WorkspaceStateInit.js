define([], function() {"use strict";

        var run = function ($rootScope, $state, $stickyState, $q, $urlRouter) {
            $rootScope.$on('$stateChangeSuccess', function(evt, toState, toParams, fromState, fromParams) {
                // initial load and is trying to load the modalstate
                var inactive = $stickyState.getInactiveStates();
                if (!inactive.find(function(state){
                        return state.name === "root.datasets.workspace";
                    })
                    && $state.includes("root.datasets")
                    && toState.name!=="root.datasets.workspace"
                    && fromState.name!=="root.datasets.workspace") {
                    // cancel initial transition
                    // evt.preventDefault();
                    // Go to the default background state. (Don't update the URL)
                    $state.go("root.datasets.workspace", undefined, { location: false }).then(function() {
                        // OK, background is loaded, now go to the original modalstate
                        $state.go(toState, toParams);
                    });
                }
            })

            var preloadStates = $state.get().filter(function(state) { return state.preload; });

            var initialPromise = $q.when(); // the start of the promise chain
            var preloadPromise = preloadStates.reduce(function (prevPromise, preloadState) {
                // builds a promise chain that goes to each "preload" state one after the other
                return prevPromise.then(function() {
                    // Chain $state.go promise on the previous promise
                    return $state.go(preloadState, undefined, {
                        location: false // Don't update url
                    });
                });
            }, initialPromise);

            preloadPromise.then(function() {
                // all states preloaded, now start listening for url changes.
                $urlRouter.listen();
            });
        };
        run.$inject = ['$rootScope', '$state', '$stickyState', '$q', '$urlRouter'];
        run.$provider="run";
        return run;
});
