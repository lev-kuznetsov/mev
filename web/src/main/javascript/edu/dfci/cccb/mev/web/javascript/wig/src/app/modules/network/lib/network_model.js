(function () {
    "use strict";

    define([], function () {

        return function () {
            var self = this;

            // Possible network states
            self.possibleStates = [0, 1, 2, 3, 4];

            // Network list
            self.list = [];

            // Network status
            self.status = {};

            // networkService errors
            self.err = 0;

        };

    });

}());