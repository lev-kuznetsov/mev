(function () {
    "use strict";

    define(['./lib/preprocess_model', './lib/preprocess_functions', './lib/preprocess_controller'],
        function (model, functions, controller) {

        angular.module('networkPreprocess', []).
            service('preprocessModel', ['$routeParams', model]).
            service('preprocessFunctions', ['$q', 'networkService', functions]).
            controller('preprocessController',
                ['$scope', 'preprocessModel', 'preprocessFunctions', '$interval', controller]);

    });

}());