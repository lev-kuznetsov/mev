(function () {
    "use strict";

    define([], function () {

        return function () {
            var self = this;

            // Network name
            self.name = '';
            // Network filename
            self.network = '';
            // User notes
            self.note = '';

            // Response message
            self.msg = '';

            // Sending form?
            self.sending = false;

        };

    });

}());