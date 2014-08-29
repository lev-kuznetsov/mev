(function () {
    "use strict";

    var def_requirements = ['angular',
        './lib/page_index', './lib/gene_ontologies', './lib/app_model', './lib/app_controller', './lib/route_config',
        'angular-route', './modules/network/network', './modules/settings/settings'];

    define(def_requirements,
        function (angular, indexService, goService, appModel, appController, routeConfig) {

        angular.module('wig-app', ['ngRoute', 'networkMGMT', 'wig-settings']).

            config(['$routeProvider', routeConfig]).

            service('indexService', ['$location', indexService]).
            service('goService', ['$http', '$q', goService]).
            service('appModel', [appModel]).
            controller('appController', ['$scope', 'appModel', 'goService', 'indexService', 'settingsService', appController]).


            directive('mainMenu', function () {
                return {
                    restrict: 'E',
                    templateUrl: '../static/wig-app/src/common/directives/main-menu.html',
                    controller: 'appController',
                    controllerAs: 'aCtrl'
                };
            });

    });

}());