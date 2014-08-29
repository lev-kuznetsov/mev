(function () {
    "use strict";

    define([], function () {

        return function () {
            var self = this;

            self.subs = {
                format: false,
                layout: false
            };

            self.inspector = false;

            self.fToggleSub = function (sub) {
                if (self.subs[sub]) {
                    self.fHideSubs();
                } else {
                    self.fHideSubs();
                    self.subs[sub] = true;
                }
            };

            self.fToggleInspector = function () {
                if (self.inspector) {
                    self.inspector = false;
                } else {
                    self.inspector = true;
                }
            };

            self.fHideSubs = function () {
                var key;
                for (key in self.subs) {
                    self.subs[key] = false;
                }
            };

        };

    });

}());