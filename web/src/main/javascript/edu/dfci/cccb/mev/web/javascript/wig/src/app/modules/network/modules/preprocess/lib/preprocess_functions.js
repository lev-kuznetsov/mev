(function () {
    "use strict";

    define([], function () {

        return function (q, networkService) {
            var self = this;

            self.check = function (name) {
                var qCheck = q.defer();

                networkService.fStatus(name).then(function (data) {
                    qCheck.resolve(data);
                });

                return qCheck.promise;
            };

            self.start = function (name) {
                networkService.fStartPreprocess(name);
            }

        };

    });

}());