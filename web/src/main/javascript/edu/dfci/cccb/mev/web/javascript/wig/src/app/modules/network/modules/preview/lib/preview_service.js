(function () {
    "use strict";

    define([], function () {

        return function (http, q, networkService) {
            var self = this;

            self.attrs = {
                percentage: 0,
                type: '',
                ing: false
            };

            self.status = {};

            self.startingNode = '';

            self.fReset = function () {
                self.attrs = {
                    percentage: 0,
                    type: '',
                    ing: false
                };
                self.startingNode = '';
            };

            // Index functions

            self.fIndexStatus = function (name, itype, iterative) {
                // Checks the status of the index building
                var qfIndexStatus = q.defer();

                http({
                    method: 'POST',
                    data: {
                        name: name,
                        itype: itype
                    },
                    url: 'index_status.json'
                })
                    .success(function (data) {
                        self.attrs.percentage = data.status.percentage;
                        qfIndexStatus.resolve(data);

                        if (iterative === true && 100 !== data.status.percentage) {
                            setTimeout(function () {
                                self.fIndexStatus(name, itype, iterative);
                            }, 1000);
                        }
                    });

                return qfIndexStatus.promise;
            };

            self.fMakeNeighborsIndex = function (name, filterPrev) {
                var qfMakeNeighborsIndex = q.defer();

                self.attrs.type = 'neighbors';
                self.attrs.ing = true;

                self.source = '';
                if (filterPrev === 1) {
                    self.source = 'f'; // Filter
                } else {
                    self.source = 'o'; // Original
                }

                http({
                    method: 'POST',
                    data: {
                        name: name,
                        source: self.source
                    },
                    url: 'index_make_neighbors.json'
                })
                    .success(function (data) {
                        self.attrs.ing = false;
                        qfMakeNeighborsIndex.resolve(data);
                    });

                // Start iterative status check
                setTimeout(function () {
                    self.fIndexStatus(name, 'neighbors', true);
                }, 1500);

                return qfMakeNeighborsIndex.promise;
            };

            self.fMakeLeavesIndex = function (name, filterPrev) {
                var qfMakeNeighborsIndex = q.defer();

                self.attrs.type = 'leaves';
                self.attrs.ing = true;

                self.source = '';
                if (filterPrev === 1) {
                    self.source = 'f'; // Filter
                } else {
                    self.source = 'o'; // Original
                }

                http({
                    method: 'POST',
                    data: {
                        name: name,
                        source: self.source
                    },
                    url: 'index_make_leaves.json'
                })
                    .success(function (data) {
                        console.log(data);
                        self.attrs.ing = false;
                        if (data.err === 4) {
                            alert('No leaves to aggregate.');
                            self.attrs.type = undefined;
                        } else if (data.err === 5) {
                            alert('Not available.');
                            self.attrs.type = undefined;
                        }
                        qfMakeNeighborsIndex.resolve(data);
                    });

                return qfMakeNeighborsIndex.promise;
            };

            // Navigation functions
            
            self.fGetVlist = function (name, filterService) {
                var qfGetVlist = q.defer();
                
                self.fPreviewStatus(name).then(function (data) {
                    if ('original' === data.status.what) {
                        filterService.fOriginalStatus(name).then(function (original) {
                            qfGetVlist.resolve(original.v_list);
                        })
                    } else if ('filtered' === data.status.what) {
                        filterService.fFilteredStatus(name).then(function (filter) {
                            qfGetVlist.resolve(filter.v_list);
                        })
                    } else if ('index' === data.status.what) {
                        self.fIndexStatus(name, self.status.type, false).then(function (index) {
                            if ('' === self.attrs.type) {
                                self.attrs.percentage = 0;
                            }
                            qfGetVlist.resolve(index.status.v_list);
                        });
                    }
                });

                return qfGetVlist.promise;
            };

            self.fPreviewStatus = function (name) {
                var qfPreviewStatus = q.defer();

                http({
                    method: 'POST',
                    data: {
                        name: name
                    },
                    url: 'view_status.json'
                })
                    .success(function (data) {
                        self.status = data.status;
                        console.log(data);
                        qfPreviewStatus.resolve(data);
                    });

                return qfPreviewStatus.promise;
            };

            // Interface functions

            self.fBackToFilters = function (name) {
                // Send the 'network' back at the FILTERING step (status == 2)
                networkService.fSetStatus(name, 2, networkService.m.possibleStates).then(function () {
                    document.location.hash = "/filter/" + name;
                });
            };

            self.fGotoViewer = function (name, sendData) {
                // Go to the Viewer
                var qfGotoViewer = q.defer();

                http({
                    method: 'POST',
                    data: sendData,
                    url: 'preset_view.json'
                })
                    .success(function (data) {
                        qfGotoViewer.resolve(data);
                    });

                return qfGotoViewer.promise;
            };

        };

    });

}());