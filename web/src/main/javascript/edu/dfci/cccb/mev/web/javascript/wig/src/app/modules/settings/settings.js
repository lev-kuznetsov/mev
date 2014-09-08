(function () {
    "use strict";

    define(['angular', './lib/controller', './lib/model', './lib/functions', './lib/service'],
        function (angular, controller, model, functions, service) {

            angular.module('wig-settings', []).
                service('settingsModel', [model]).
                service('settingsFunctions', ['$http', '$q', functions]).
                service('settingsService', ['settingsModel', 'settingsFunctions', service]).
                controller('settingsController', ['$scope', 'settingsModel', 'settingsFunctions', controller]);

    });

}());