(function () {
    "use strict";

    define([], function () {

        return function (scope, model, goService, indexService, settingsService) {

            scope.m = model;
            scope.index = indexService;

            // Check GO2GA dictionary
            goService.make().then(function (data) {
                scope.m.goResponse = data;
            });

            // Check WIG settings
            settingsService.f.get().then(function (data) {
                if (data.max_nodes === undefined) {
                    settingsService.f.set('max_nodes', settingsService.m.settings.max_nodes);
                }
            });

        };

    });

}());