(function () {
    "use strict";

    define([], function () {

        return function () {
            var self = this;

            self.network_name = '';

            self.response_msg = '';
            self.response_class = '';

            self.counter = [];
            self.filter = '';
            self.gos = [];
            self.tmpGO = '';
            self.names = [];
            self.tmpName = '';

        };

    });

}());