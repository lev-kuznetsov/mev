(function () {
    "use strict";

    define([], function () {

        return function (http, q) {
        	var self = this;

        	// GET functions
        	
            self.get = function () {
                var qcheck = q.defer();

                http({

                    method: 'GET',
                    url: 'get_settings.json'

                })
                    .success(function (data) {
                        qcheck.resolve(data);
                    });

                return qcheck.promise;
            };

        	// SET functions

            self.set = function (name, val) {
                var qset = q.defer();

                http({

                    method: 'POST',
                    data: {
                        name: name,
                        val: val
                    },
                    url: 'set_setting.json'

                })
                    .success(function (data) {
                        console.log(data);
                        qset.resolve(data);
                    });

                return qset.promise;
            };

            self.multiSet = function (opts) {
                var qmultiSet = q.defer();

                http({

                    method: 'POST',
                    data: {
                        opts: opts
                    },
                    url: 'set_settings.json'

                })
                    .success(function (data) {
                        console.log(data);
                        qmultiSet.resolve(data);
                    });

                return qmultiSet.promise;
            };

        };

    });

}());