(function () {
    "use strict";

    define([], function () {

        return function (http, q) {
            var self = this;

            self.make = function () {
                var qmake = q.defer();

                http({
                    method: 'GET',
                    url: 'go_make.json'
                })
                    .success(function (data) {
                        qmake.resolve(data);
                    })
                    .error(function (error) {
                        qmake.reject(error);
                    });

                return qmake.promise;
            };
            
        };

    });

}());