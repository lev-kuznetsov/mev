(function () {
    "use strict";

    define([], function () {

        return function (scope, model, routeParams, toolbarService, canvasService, networkService) {
            scope.m = model;

            scope.m.network_name = routeParams.name;
            
            scope.t = toolbarService;
            scope.c = canvasService;

            scope.fInit = function (name) {
                scope.c.fGetStatus(name).then(function (data) {
                    scope.c.fInit(name).then(function (data) {
                        setTimeout(function () {
                            window.cy.resize();
                            window.cy.fit();
                        }, 1000);
                    });
                });

            };

            scope.fBacktoPreview = function (name) {
                networkService.fSetStatus(name, 3, networkService.m.possibleStates).then(function (data) {
                    document.location.hash = '#/preview/' + scope.m.network_name;
                });
            };

            scope.$on('showInspector', function () {
                scope.t.inspector = true;
                scope.$apply();
            });

            scope.fInit(scope.m.network_name);
        };

    });

}());