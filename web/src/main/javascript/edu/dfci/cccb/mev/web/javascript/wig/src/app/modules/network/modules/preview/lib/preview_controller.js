(function () {
    "use strict";

    define([], function () {

        return function (scope, model, routeParams, filterService, previewService, settingsService) {
            scope.m = model;
            scope.s = previewService;
            scope.fs = filterService;

            scope.m.network_name = routeParams.name;
            scope.s.fReset();
            scope.s.fGetVlist(scope.m.network_name, filterService).then(function (data) {
                scope.s.vlist = data;
            });

            scope.fs.fOriginalStatus(scope.m.network_name).then(function (original) {
                scope.m.original = original;
                scope.fs.fFilteredStatus(scope.m.network_name).then(function (filter) {
                    scope.m.filter = filter;
                    if (filter.prev === 1) {
                        scope.m.what = 'filtered';
                    } else {
                        scope.m.what = 'original';
                    }

                    settingsService.f.get().then(function (data) {
                        scope.m.max_nodes = parseInt(data.max_nodes, 10);
                    });
                });
            });

            scope.fGotoViewer = function (name, what, how, type) {

                // Check input parameters
                if (-1 === ['original', 'filtered', 'index'].indexOf(what) || -1 === ['whole', 'navigate'].indexOf(how)) {
                    qfGotoViewer.resolve(null);
                    return qfGotoViewer.promise;
                }

                var sendData = {
                    name: name,
                    what: what,
                    how: how,
                    type: type,
                    start: scope.s.startingNode,
                    attrs: {
                        what: what,
                        how: how,
                        type: type,
                        start: scope.s.startingNode
                    }
                };
                
                if (what === 'index') {

                    if ('' !== scope.s.startingNode && 'navigate' == how) {
                        scope.s.fGotoViewer(name, sendData).then(function (data) {
                            if (0 === data.err) {
                                if (-1 !== ['navigate', 'whole'].indexOf(how)) {
                                    document.location.hash = '/view/' + name;
                                } else {
                                    data.err = 5;
                                }
                            }
                        });
                    } else {
                        scope.s.fGotoViewer(name, sendData).then(function (data) {
                            if (0 === data.err) {
                                if ('navigate' === how) {
                                    document.location.hash = '#/preview/' + name + '/chooseCenter';
                                } else if ('whole' === how) {
                                    document.location.hash = '#/view/' + name;
                                } else {
                                    data.err = 5;
                                }
                            }
                        });
                    }

                } else {

                    var persevere = true;
                    if (how === 'whole') {
                        var n;
                        if (scope.m.filter.prev !== 1) {
                            n = scope.m.original.v_count;
                        } else {
                            n = scope.m.filter.v_count;
                        }
                        if (n > scope.m.max_nodes) {
                            console.log('Too many nodes.');
                            alert('Too many nodes.');
                            persevere = false;
                        }

                        if (persevere) {
                            scope.s.fGotoViewer(name, sendData).then(function (data) {
                                if (0 === data.err) {
                                    if ('whole' === how) {
                                        document.location.hash = '/view/' + name;
                                    } else {
                                        data.err = 5;
                                    }
                                }
                            });

                        }
                    } else if ('navigate' == how) {
                        if ('' !== scope.s.startingNode) {

                            scope.s.fGotoViewer(name, sendData).then(function (data) {
                                if (0 === data.err) {
                                    if ('navigate' === how) {
                                        document.location.hash = '/view/' + name;
                                    } else {
                                        data.err = 5;
                                    }
                                }
                            });

                        } else {

                            scope.s.fGotoViewer(name, sendData).then(function (data) {
                                if (0 === data.err) {
                                    if ('navigate' === how) {
                                        document.location.hash = '/preview/' + name + '/chooseCenter';
                                    } else {
                                        data.err = 5;
                                    }
                                }
                            });

                        }
                    }

                }
            };

        };

    });

}());