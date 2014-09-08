(function () {
    "use strict";

    define([], function () {

        return function (http, q, networkService) {
            var self = this;

            self.isInit = false;
            self.isFiltering = false;
            self.status = {
                e_count: 0,
                err: -1,
                ew_chart: "",
                ew_nbins: 0,
                ew_rawdata: "",
                fattr: {},
                fstatus: 0,
                gene_onto: "",
                prev: 0,
                v_count: 0,
                v_list: []
            };

            // General filter functions
            
            self.fIsInit = function (name) {
                // Checks if a filter MGMT instance has been initiated for the given network
                var qfIsInit = q.defer();

                http({
                    method: 'POST',
                    data: {
                        name: name
                    },
                    url: 'is_filter_init.json'
                })
                    .success(function (data) {
                        if (data.err === 2) {
                            self.isInit = true;
                        } else {
                            self.isInit = false;
                        }
                        qfIsInit.resolve(data);
                    });

                return qfIsInit.promise;
            };

            self.fInit = function (name) {
                // Initializes filter MGMT for the given network
                var qfInit = q.defer();

                self.fIsInit(name).then(function () {
                    if (!self.isInit) {

                        // Initialize
                        http({
                            method: 'POST',
                            data: {
                                name: name
                            },
                            url: 'filter_init.json'
                        })
                            .success(function (data) {
                                if (data.err === 0) {
                                    self.isInit = true;
                                }
                                qfInit.resolve(data);
                            });

                    } else {

                        self.fFilteredStatus(name, true).then(function () {
                            qfInit.resolve(self.status);
                        });

                    }
                });

                return qfInit.promise;
            };

            self.fOriginalStatus = function (name) {
                // Checks the status of the original network
                var qfOriginalStatus = q.defer();

                self.network_name = name;

                networkService.fStatus(self.network_name).then(function (data) {
                    self.network = data;
                    qfOriginalStatus.resolve(data);
                });

                return qfOriginalStatus.promise;
            };

            self.fFilteredStatus = function (name, doConsole) {
                // Checks the status of the current filtered network
                var qfFilteredStatus = q.defer();

                self.fIsFiltering(name).then(function () {
                    if (self.isFiltering) {

                        http({
                            method: 'POST',
                            data: {
                                name: name
                            },
                            url: 'filter_status.json'
                        })
                            .success(function (data) {
                                self.status = data;

                                // Reset ew_chart if it was nullified by mistake
                                if (self.status.ew_chart === null) {
                                    self.status.ew_chart = self.network.ew_chart;
                                }
                                // Reset gene_onto if it was nullified by mistake
                                if (self.status.gene_onto === null) {
                                    self.status.gene_onto = self.network.gene_onto;
                                }

                                // Resets filter attributes if retrieving previous data
                                if (data.prev === 1) {
                                    self.status.lastFattr = self.status.fattr;
                                    self.status.fattr = {};
                                }

                                if (doConsole) { console.log(data); }
                                qfFilteredStatus.resolve(data);
                            });

                    } else {
                        qfFilteredStatus.reject(null);
                    }
                });

                return qfFilteredStatus.promise;
            };

            self.fIsFiltering = function (name) {
                // Checks if there is a current filter_try
                // 
                // This is just for debugging purposes, once the filterMGMT is initiated,
                // a filter_try should be automaticlaly initiated too
                var qfIsFiltering = q.defer();

                http({
                    method: 'POST',
                    data: {
                        name: name
                    },
                    url: 'is_filtering.json'
                })
                    .success(function (data) {
                        if (data.err === 3) {
                            self.isFiltering = true;
                        }
                        qfIsFiltering.resolve(data);
                    });

                return qfIsFiltering.promise;
            };

            self.fApplyFilter = function (name) {
                // Applies the current filter_try
                var qfApplyFilter = q.defer();

                self.fIsFiltering(name).then(function () {
                    if (self.isFiltering) {

                        http({
                            method: 'POST',
                            data: {
                                name: name
                            },
                            url: 'filter_apply.json'
                        })
                            .success(function (data) {
                                self.status.fattr.applyErr = data.err;
                                if (data.err === 0) {
                                    self.status.fattr.applyMsg = 'Filter correctly applied.';
                                } else {
                                    self.status.fattr.applyMsg = 'The filter was not applied. Try again...';
                                }
                                qfApplyFilter.resolve(data);
                            });

                    } else {
                        qfApplyFilter.resolve();
                    }
                });

                return qfApplyFilter.promise;
            };

            self.fResetFilter = function (name) {
                var qfResetFilter = q.defer();

                http({
                    method: 'POST',
                    data: {
                        name: name
                    },
                    url: 'filter_reset.json'
                })
                    .success(function (data) {
                        qfResetFilter.resolve(data);
                    });

                return qfResetFilter.promise;
            };

            self.fResetTry = function (name) {
                var qfResetTry = q.defer();

                http({
                    method: 'POST',
                    data: {
                        name: name
                    },
                    url: 'filter_reset_try.json'
                })
                    .success(function (data) {
                        qfResetTry.resolve(data);
                    });

                return qfResetTry.promise;
            }

            // Specific filter functions

            self.fTryEwThr = function (name) {
                var qfTryEwThr = q.defer();

                // Set specific filter attributes in self.status.fattr
                self.status.fattr.type = 'ewThr';
                self.status.fstatus = -1;

                http({
                    method: 'POST',
                    data: {
                        name: name,
                        thr: self.status.fattr.thr
                    },
                    url: 'filter_ewThr.json'
                })
                    .success(function (data) {
                        self.fFilteredStatus(name);
                        qfTryEwThr.resolve(data);
                    });

                return qfTryEwThr.promise;
            };

            self.fTryGOwl = function (name, golist) {
                var qfTryGOwl = q.defer();

                // Set specific filter attributes in self.status.fattr
                self.status.fattr.type = 'GOwl';
                self.status.fstatus = -1;

                http({
                    method: 'POST',
                    data: {
                        name: name,
                        golist: golist
                    },
                    url: 'filter_GOwl.json'
                })
                    .success(function (data) {
                        console.log(data);
                        self.fFilteredStatus(name, true);
                        qfTryGOwl.resolve(data);
                    });

                return qfTryGOwl.promise;
            };

            self.fTryGObl = function (name, golist) {
                var qfTryGObl = q.defer();

                // Set specific filter attributes in self.status.fattr
                self.status.fattr.type = 'GObl';
                self.status.fstatus = -1;

                http({
                    method: 'POST',
                    data: {
                        name: name,
                        golist: golist
                    },
                    url: 'filter_GObl.json'
                })
                    .success(function (data) {
                        console.log(data);
                        self.fFilteredStatus(name, true);
                        qfTryGObl.resolve(data);
                    });

                return qfTryGObl.promise;
            };

            self.fTryNwl = function (name, namelist) {
                var qfTryNwl = q.defer();

                http({
                    method: 'POST',
                    data: {
                        name: name,
                        list: namelist
                    },
                    url: 'filter_Nwl.json'
                })
                    .success(function (data) {
                        console.log(data);
                        self.fFilteredStatus(name, true);
                        qfTryNwl.resolve(data);
                    });

                return qfTryNwl.promise;
            };

            self.fTryNbl = function (name, namelist) {
                var qfTryNbl = q.defer();

                http({
                    method: 'POST',
                    data: {
                        name: name,
                        list: namelist
                    },
                    url: 'filter_Nbl.json'
                })
                    .success(function (data) {
                        console.log(data);
                        self.fFilteredStatus(name, true);
                        qfTryNbl.resolve(data);
                    });

                return qfTryNbl.promise;
            };

        };

    });

}());