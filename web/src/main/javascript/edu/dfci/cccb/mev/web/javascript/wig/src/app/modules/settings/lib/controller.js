(function () {
    "use strict";

    define([], function () {

        return function (scope, model, functions) {
        	scope.m = model;
            scope.f = functions;

            scope.f.get().then(function (data) {
                scope.m.settings.max_nodes = parseInt(data.max_nodes, 10);
            });

            scope.f.apply = function (opts, msg, duration) {
                scope.f.multiSet(opts).then(function (data) {
                    scope.m.formMsg = msg;
                    setTimeout(function () {
                        scope.m.formMsg = '';
                    }, duration);
                });
            };

        };

    });

}());