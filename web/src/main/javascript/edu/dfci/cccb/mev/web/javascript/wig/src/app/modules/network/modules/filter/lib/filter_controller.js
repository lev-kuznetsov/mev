(function () {
    "use strict";

    define([], function () {

        return function (scope, model, routeParams, filterService, networkService) {
        	scope.m = model;
        	scope.s = filterService;

            scope.m.network_name = routeParams.name;

            // Check original network status
            scope.s.fOriginalStatus(scope.m.network_name).then(function () {
                scope.m.original = scope.s.network;

                // Initialize filterMGMT if necessary
                scope.s.fInit(scope.m.network_name).then(function () {

                });

            });

            // Generic functions

            scope.apply = function (name) {
                var res = confirm('Do you want to apply the current filter?');

                if (res) {
                    scope.m.response_msg = '...applying...';
                    scope.m.response_class = 'text-info';
                    scope.s.status.fstatus = -1;

                    scope.s.fApplyFilter(name).then(function () {
                        scope.s.status.fstatus = 1;

                        scope.response_msg = scope.s.status.fattr.applyMsg;
                        if (scope.s.status.fattr.applyErr === 0) {
                            scope.response_class = 'text-success';
                            document.location.hash = "/filter/" + name;
                        } else {
                            scope.response_class = 'text-danger';
                        }

                        setTimeout(function () {
                            scope.response_msg = '';
                            scope.response_class = '';
                        }, 5000);
                    });
                }
            };

            scope.continue = function (name) {
                networkService.fSetStatus(name, 3, networkService.m.possibleStates).then(function (data) {
                    if (data.err === 0) {
                        document.location.hash = "/preview/" + name;
                    }
                });
            };

            scope.reset = function (name) {
                scope.s.fResetFilter(name).then(function () {
                    scope.s.fOriginalStatus(name).then(function () {
                        scope.original = scope.s.network;
                        scope.s.status.ew_chart = scope.original.ew_chart;

                        // Initialize filterMGMT if necessary
                        scope.s.fInit(name).then(function () {
                            scope.s.fFilteredStatus(name);
                        });

                    });
                });
            };

            scope.resetTryGotoHash = function (name, hash) {
                scope.s.fResetTry(name).then(function (data) {
                    if (data.err == 0) {
                        document.location.hash = hash;
                    }
                });
            };

            // Specific filter functions

            scope.tryEwThr = function (name) {
                scope.m.response_msg = '...processing...';
                scope.m.response_class = 'text-info';

                scope.s.fTryEwThr(name).then(function () {
                    scope.m.response_msg = 'Done!';
                    scope.m.response_class = 'text-success';

                    setTimeout(function () {
                        scope.m.response_msg = '';
                        scope.m.response_class = '';
                    }, 5000);
                });
            };

            scope.addGO = function (go) {
                if ('' !== go && -1 === scope.m.gos.indexOf(go)) {
                    scope.m.filter = "";
                    scope.m.gos.push(go);
                    scope.m.counter.push(scope.m.counter.length);
                }
                scope.m.tmpGO = '';
            };

            scope.popGO = function (i) {
                scope.m.gos.splice(i, 1);
                scope.m.counter.splice(scope.m.counter.length-1, 1);
            };

            scope.resetGO = function () {
                scope.m.counter = [];
                scope.m.filter = '';
                scope.m.gos = [];
                scope.m.tmpGO = '';
            }

            scope.tryGOwl = function (name, golist) {
                scope.m.response_msg = '...processing...';
                scope.m.response_class = 'text-info';

                scope.s.fTryGOwl(name, golist).then(function () {
                    scope.m.response_msg = 'Done!';
                    scope.m.response_class = 'text-success';

                    setTimeout(function () {
                        scope.m.response_msg = '';
                        scope.m.response_class = '';
                    }, 5000);
                });
            };

            scope.tryGObl = function (name, golist) {
                scope.m.response_msg = '...processing...';
                scope.m.response_class = 'text-info';

                scope.s.fTryGObl(name, golist).then(function () {
                    scope.m.response_msg = 'Done!';
                    scope.m.response_class = 'text-success';

                    setTimeout(function () {
                        scope.m.response_msg = '';
                        scope.m.response_class = '';
                    }, 5000);
                });
            };

            scope.addName = function (name) {
                if ('' !== name && -1 === scope.m.names.indexOf(name)) {
                    scope.m.filter = "";
                    scope.m.names.push(name);
                    scope.m.counter.push(scope.m.counter.length);
                }
                scope.m.tmpName = '';
            };

            scope.popName = function (name) {
                scope.m.names.splice(i, 1);
                scope.m.counter.splice(scope.m.counter.length-1, 1);
            };

            scope.resetName = function () {
                scope.m.counter = [];
                scope.m.filter = '';
                scope.m.names = [];
                scope.m.tmpName = '';
            };

            scope.tryNwl = function (name, namelist) {
                scope.m.response_msg = '...processing...';
                scope.m.response_class = 'text-info';

                scope.s.fTryNwl(name, namelist).then(function () {
                    scope.m.response_msg = 'Done!';
                    scope.m.response_class = 'text-success';

                    setTimeout(function () {
                        scope.m.response_msg = '';
                        scope.m.response_class = '';
                    }, 5000);
                });
            };

            scope.tryNbl = function (name, namelist) {
                scope.m.response_msg = '...processing...';
                scope.m.response_class = 'text-info';

                scope.s.fTryNbl(name, namelist).then(function () {
                    scope.m.response_msg = 'Done!';
                    scope.m.response_class = 'text-success';

                    setTimeout(function () {
                        scope.m.response_msg = '';
                        scope.m.response_class = '';
                    }, 5000);
                });
            };

        };

    });

}());