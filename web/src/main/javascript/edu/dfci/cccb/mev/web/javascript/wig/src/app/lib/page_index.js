(function () {
    "use strict";

    define([], function () {

        return function (location) {
            var self = this;

            self.index = 0;

            self.setIndex = function (v) {
                self.index = v;
            };

            self.isIndex = function (v) {
                return self.index === v;
            };

            self.isLocation = function (v) {
                return v === location.path();
            };
        };

    });

}());