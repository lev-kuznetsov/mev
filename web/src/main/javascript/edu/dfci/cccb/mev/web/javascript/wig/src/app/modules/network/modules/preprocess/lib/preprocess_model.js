(function () {
    "use strict";

    define([], function () {

        return function (routeParams) {
            var self = this;

            self.name = routeParams.name;
            self.duration = 0;
            self.status = '-1';
            self.status_label = ['> Starting...'];
            self.percentage = 0;

        };

    });

}());