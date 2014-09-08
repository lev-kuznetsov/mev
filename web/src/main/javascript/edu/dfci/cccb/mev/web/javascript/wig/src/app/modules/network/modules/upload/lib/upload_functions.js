(function () {
    "use strict";

    define([], function () {

        return function (http, q) {
            var self = this;

            self.send = function (name, network, note) {
                var qSubmit = q.defer();

                http({
                    method: 'POST',
                    url: 'upload',
                    data: {
                        name: name,
                        network: network,
                        note: note
                    },
                    headers: { 'Content-Type': undefined },
                    transformRequest: function (data) {
                        var fd = new FormData();
                        angular.forEach(data, function (value, key) {
                            fd.append(key, value);
                        });
                        return fd;
                    }
                })
                    .success(function (data) {
                        qSubmit.resolve(data);
                    });

                return qSubmit.promise;
            };
            
        };

    });

}());