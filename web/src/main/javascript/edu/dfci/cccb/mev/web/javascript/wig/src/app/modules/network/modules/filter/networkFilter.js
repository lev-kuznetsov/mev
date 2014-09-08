(function () {
    "use strict";

    define(['./lib/filter_service', './lib/filter_model', './lib/filter_controller'],
        function (service, model, controller) {

        angular.module('networkFilter', []).
            service('filterService', ['$http', '$q', 'networkService', service]).
            service('filterModel', model).
            controller('filterController', ['$scope', 'filterModel', '$routeParams', 'filterService', 'networkService', controller]);

    });

}());