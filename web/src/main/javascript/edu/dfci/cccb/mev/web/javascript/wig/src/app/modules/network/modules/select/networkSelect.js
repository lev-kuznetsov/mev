(function () {
    "use strict";

    define(['./lib/selectController'], function (selectController) {

        angular.module('networkSelect', []).
            controller('selectController', ['$scope', 'networkService', selectController]);

    });

}());