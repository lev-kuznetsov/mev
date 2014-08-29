(function () {
    "use strict";

    define([], function () {

        return function (http, q, model) {
            var self = this;
            self.m = model;

            self.fList = function () {
                // Retrieves the network list
                var qflist = q.defer();

                http({

                    method: 'POST',
                    url: 'list_networks.json'

                })
                    .success(function (data) {
                        self.m.list = data.list;
                        qflist.resolve(data);
                    });

                return qflist.promise;
            };

            self.fStatus = function (name) {
                // Retrieves the status of a given network
                var qfstatus = q.defer();

                http({

                    method: 'POST',
                    data: {
                        name: name
                    },
                    url: 'status.json'

                })
                    .success(function (data) {
                        self.m.status = data;
                        qfstatus.resolve(data);
                    });

                return qfstatus.promise;
            };

            self.fSetStatus = function (name, status, possible) {
                // Sets the status of a given network
                var qfSetStatus = q.defer();

                if (possible.indexOf(status) !== -1) {

                    http({
                        method: 'POST',
                        data: {
                            name: name,
                            status: status
                        },
                        url: 'set_status.json'
                    })
                        .success(function (data) {
                            self.m.err = data.err;
                            qfSetStatus.resolve(data);
                        });

                } else {
                    qfSetStatus.reject(null);
                }

                return qfSetStatus.promise;
            };

            self.fStartPreprocess = function (name) {
                // Starts the preprocessing of a given network
                var qfstartPreprocess = q.defer();

                http({

                    method: 'POST',
                    data: {
                        name: name
                    },
                    url: 'preprocess.json'

                })
                    .success(function (data) {
                        console.log(data);
                        qfstartPreprocess.resolve(data);
                    });

                return qfstartPreprocess.promise;
            };
            
        };

    });

}());