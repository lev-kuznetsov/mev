define(['angular', 'lodash', 'angular-resource', '../dataset/lib/AnalysisClass'], function (angular, _, angularResource, AnalysisClass) {

    return angular
        .module('Mev.Api', ['ngResource'])
        // .service('AnalysisEventBus', AnalysisEventBus)
        .service('DatasetResourceCache', ["mevDb", function (mevDb) {
            this.get = function (datasetId) {
                return mevDb.getDataset(datasetId);
            };
            this.put = function (dataset) {
                return mevDb.putDataset(dataset);
            }
        }])
        .service('DatasetResourceService', ['$resource', '$q', '$http', '$rootScope', 'DatasetResourceCache', "mevDb",
            function ($resource, $q, $http, $rootScope, DatasetResourceCache, mevDb) {
                var resource = $resource('/dataset/:datasetName/data', {format: "json"},
                    {
                        'get': {method: 'GET'},
                        'getAll': {
                            url: '/dataset',
                            method: 'GET',
                            isArray: true
                        },
                        'subset': {
                            url: '/dataset/:datasetName/data/subset/export',
                            method: "POST"
                        }
                    });
                var DatasetResource = Object.create(resource);

                DatasetResource.get = function (params, data, callback) {
                    var deferred = $q.defer();
                    var cache = {
                        $promise: deferred.promise,
                        $resolved: false
                    };
                    var cachePromise = DatasetResourceCache.get(params.datasetName)
                        .catch(function (e) {
                            if (e.status === 404 || e.status === 501) {
                                _.assign(cache, resource.get(params, data, callback))
                                return cache.$promise.then(function (response) {
                                    DatasetResourceCache.put(_.assign(response, {id: params.datasetName}));
                                    return response;
                                });
                            }
                        }).then(function (response) {
                            return deferred.resolve(
                                _.extend(response, {
                                    $promise: cache.$promise
                                })
                            );
                        });
                    return cache;
                };
                DatasetResource.getAll = function (params, data, callback) {
                    var datasetsResource = resource.getAll(params, data, callback);
                    datasetsResource.$promise.then(function (response) {
                        $rootScope.$broadcast("mev:datasets:list:refreshed", response);
                    });
                    return datasetsResource;
                };
                DatasetResource.subset = function (params, data, callback) {
                    data.name = params.datasetName + "--" + data.name;
                    var datasetsResource = resource.subset(params, data, callback);
                    datasetsResource.$promise.then(function (response) {
                        $http({
                            method: "POST",
                            url: "/annotations/" + params.datasetName + "/annotation/row"
                            + "/export?destId=" + data.name
                        });
                        $http({
                            method: "POST",
                            url: "/annotations/" + params.datasetName + "/annotation/column"
                            + "/export?destId=" + data.name
                        });
                        DatasetResource.getAll();
                    })
                    return datasetsResource;
                };
                DatasetResource.uploadFile = function (file) {
                    var formdata = new FormData;
                    formdata.append('upload', file);
                    formdata.append('name', file.name);
                    var xhr = new XMLHttpRequest();
                    xhr.upload.addEventListener("progress", function (e) {
                        return;
                    });
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState == 4 && xhr.status == 200) {
                            DatasetResource.getAll();
                        }
                        ;
                    };
                    xhr.open("POST", "/dataset", true);
                    xhr.send(formdata);
                };
                DatasetResource.activate = function(dataset){
                    return mevDb.getDataset(dataset.id)
                        .then(function(dataset){
                            return mevDb.getDatasetValues64(dataset.id)
                                .then(function(values){
                                    var formdata = new FormData();
                                    formdata.append('name', dataset.id);
                                    formdata.append('rows', dataset.row.keys);
                                    formdata.append('rowSelections', JSON.stringify(dataset.row.selections));
                                    // formdata.append('rowSelections', new Blob([JSON.parse(JSON.stringify(dataset.row.selections))],
                                    //     {
                                    //         type: "application/json"
                                    //     })
                                    // );
                                    formdata.append('columns', dataset.column.keys);
                                    formdata.append('columnSelections', JSON.stringify(dataset.column.selections));

                                    formdata.append('upload', values);
                                    var xhr = new XMLHttpRequest();

                                    xhr.upload.addEventListener("progress", function (e) {
                                        return;
                                    });
                                    xhr.onreadystatechange = function () {
                                        if (xhr.readyState == 4 && xhr.status == 200) {
                                            $rootScope.$broadcast("mev:dataset:activated", dataset);
                                            DatasetResource.getAll();
                                        }
                                        ;
                                    };
                                    xhr.open("POST", "/import/binary", true);
                                    xhr.send(formdata);
                                });                                
                        });
                }
                return DatasetResource;
            }])
        .service('GoogleDriveResourceService', ['$resource', function ($resource) {
            return $resource('/import/google',
                {
                    'format': 'json'
                }, {
                    'get': {method: 'GET'},
                    'post': {
                        url: '/import/google/:id/load',
                        method: 'POST'
                    }
                });
        }])
        .service('AnalysisResourceService', ['$resource', '$http', '$routeParams', "$timeout", 'mevAnalysisEventBus',
            function ($resource, $http, $routeParams, $timeout, analysisEventBus) {

                var transformRequest = [function (data, headers) {
                    console.log("transformRequest", data);
                    return data;
                }].concat($http.defaults.transformRequest);

                var resource = $resource('/dataset/:datasetName/analysis',
                    {'format': 'json'},
                    {
                        'getAll': {
                            'url': '/dataset/:datasetName/analysis',
                            'method': "GET",
                        },
                        'get': {
                            'url': '/dataset/:datasetName'
                            + '/analysis/:analysisName',
                            'method': "GET"
                        },
                        'postf': {
                            'method': 'POST',
                            'url': '/dataset/:datasetName'
                            + '/analyze/:analysisType/:analysisName(:analysisParams)'
                        },
                        'post': {
                            'method': 'POST',
                            'url': '/dataset/:datasetName'
                            + '/analyze/:analysisType'
                        },
                        'post3': {
                            'method': 'POST',
                            'url': '/dataset/:datasetName' +
                            '/analyze/:analysisType/:analysisName',
                            //'headers':{'Content-Type':'application/x-www-form-urlencoded'}
                        },
                        'post4': {
                            'method': 'GET',
                            'url': '/dataset/:datasetName' +
                            '/analyze/:analysisType/:analysisName',
                            //'headers':{'Content-Type':'application/x-www-form-urlencoded'}
                        },
                        'put': {
                            'method': 'PUT',
                            'url': '/dataset/:datasetName' +
                            '/analyze/:analysisType/:analysisName'
                        },
                        'delete': {
                            'method': 'DELETE',
                            'url': '/dataset/:datasetName/analysis/:analysisName'
                        }

                    });

                var AnalysisResource = Object.create(resource);
                AnalysisResource.post = postWrapper("post");
                AnalysisResource.postf = postWrapper("postf");
                AnalysisResource.post3 = postWrapper("post3");
                AnalysisResource.put = postWrapper("put");

                function postWrapper(methodName) {
                    return function (params, data, callback) {
                        if (params.analysisName && params.analysisName.toLowerCase() === params.analysisType.toLowerCase()) {
                            //do not prefix analysis name with type - name already contains the type
                        } else {
                            if (params.analysisName)
                                params.analysisName = params.analysisType + "_" + params.analysisName;
                            if (data.name)
                                data.name = params.analysisType + "_" + data.name;

                        }

                        var result = resource[methodName](params, data, callback);

                        result.$promise.then(
                            function (response) {

                                if (typeof data === "string")
                                    data = JSON.parse(data);
                                if (Array.isArray(data))
                                    data = {data: data};
                                var allParams = {
                                    analysisName: params.analysisName || data.analysisName || params.name || data.name || response.name
                                };
                                angular.extend(allParams, params);

                                angular.extend(allParams, data);
                                console.debug("AnalysisResource success", params, "data", data, "response", response);
                                var sessionStorageKey = allParams.datasetName + "." + allParams.analysisName;
                                console.debug("sessionStorageKey set", sessionStorageKey);
                                sessionStorage.setItem(sessionStorageKey, JSON.stringify(allParams));

                                function poll(prevResponse, wait) {
                                    if (prevResponse.status && prevResponse.status === "IN_PROGRESS") {
                                        $timeout(function () {
                                            var pollParams = {
                                                datasetName: allParams.datasetName,
                                                analysisName: allParams.analysisName
                                            };
                                            AnalysisResource.get(pollParams,
                                                function (newResponse) {
                                                    poll(newResponse, 5000);
                                                });
                                        }, wait);
                                    } else {
                                        var analysis = new AnalysisClass(prevResponse);
                                        if (analysis.params)
                                            angular.extend(analysis.params, allParams);
                                        else
                                            analysis.params = allParams;
                                        if (prevResponse.status === "ERROR") {
                                            console.error("PollAnalysis error", analysis.name, analysis);
                                            analysisEventBus.analysisFailed(params, data, analysis);
                                        } else {
                                            console.log("PollAnalysis result", analysis.name, analysis);
                                            analysisEventBus.analysisSucceeded(params, data, analysis);
                                        }

                                    }
                                };

//    					analysisEventBus.analysisStarted(response);
                                analysisEventBus.analysisStarted(allParams.analysisType, allParams.analysisName, new AnalysisClass(response));
                                poll(response, 500);


                            }, function (response) {
                                console.debug("AnalysisResource error", response);
                                analysisEventBus.analysisFailed(params, data);
                            }
                        );

                        return result;
                    };
                }


                return AnalysisResource;

            }])
        .service('SelectionResourceService', ['$resource', '$routeParams', '$http', 'mevDatasetRest', '$rootScope',
            '$q', 'mevWorkspace', 'mevDb',
            function ($resource, $routeParams, $http, datasetResource, $rootScope, $q, mevWorkspace, mevDb) {

                var resource = $resource('/dataset/:datasetName/:dimension/selection', {
                    'format': 'json'
                }, {
                    'getAll': {
                        'url': '/dataset/:datasetName/:dimension/selections',
                        'method': 'GET'
                    },
                    'get': {
                        'method': 'GET',
                        'url': '/dataset/:datasetName/:dimension/selection/:selectionName'
                    },
                    'post': {
                        'method': 'POST',
                        'url': "/dataset/:datasetName/:dimension/selection/",
                    },
                    'put': {
                        'method': 'PUT',
                        'url': "/dataset/:datasetName/:dimension/selection/",
                    },
                    'export': {
                        'method': 'POST',
                        'url': "/dataset/:datasetName/:dimension/selection/export",
                    },
                    'delete': {
                        'method': 'DELETE',
                        'url': '/dataset/:datasetName/:dimension/selection/:selectionName'
                    }

                });

//    	return resource;
                var SelectionResource = Object.create(resource);
                SelectionResource.post = function (params, data, callback) {
                    var result = resource.post(params, data, callback);
                    result.$promise.then(function (response) {
                        $rootScope.$broadcast("mui:dataset:selections:added", params.dimension, params, data, response);
                    });
                    return result;
                };
                SelectionResource.put = function (params, data, callback) {
                    var result = resource.put(params, data, callback);
                    result.$promise.then(function (response) {
                        $rootScope.$broadcast("mui:dataset:selections:added", params.dimension, params, data, response);
                    });
                    return result;
                };
                SelectionResource.getAll = function (params, callback) {
                    var deferred = $q.defer();
                    var cache = {
                        $promise: deferred.promise,
                        $resolved: false
                    };
                    mevWorkspace.getDataset(params.datasetName)
                        .then(function (dataset) {
                            if (dataset && dataset.isActive) {
                                return resource.getAll(params).$promise
                                    .then(function (response) {
                                        return response.selections.map(function (selection) {
                                            selection.type = params.dimension;
                                            return selection;
                                        });
                                    });
                            } else {
                                return [];
                            }
                        })
                        .then(function (remote) {
                            return mevDb.getDataset(params.datasetName)
                                .catch(function(e){
                                    if(e.status === 501)
                                        return undefined;
                                    else
                                        throw e;
                                })
                                .then(function (dataset) {
                                    var remoteAndLocal = dataset
                                        ? _.unionBy(
                                        remote,
                                        dataset[params.dimension].selections,
                                        function (selection) {
                                            return selection.name;
                                        })
                                        : remote;
                                    if(callback)
                                        callback({selections: remoteAndLocal})
                                    deferred.resolve(remoteAndLocal);
                                    return remoteAndLocal;
                                })
                        })
                        .catch(function (e) {
                            console.error("Error fetching selection list: ", params, e);
                            deferred.reject(e);
                            throw e;
                        });

                    return cache;
                };
                SelectionResource.export = function (params, data, callback) {
                    data.name = params.datasetName + "--" + data.name;
                    var result = resource.export(params, data, callback);
                    result.$promise.then(function (response) {
                        $http({
                            method: "POST",
                            url: "/annotations/" + params.datasetName + "/annotation/row"
                            + "/export?destId=" + data.name
                        });
                        $http({
                            method: "POST",
                            url: "/annotations/" + params.datasetName + "/annotation/column"
                            + "/export?destId=" + data.name
                        });
                        datasetResource.getAll();
                    })
                }
                SelectionResource.delete = function(params, data, callback){
                    var deferred = $q.defer();
                    var cache = {
                        $promise: deferred.promise,
                        $resolved: false
                    };
                    mevWorkspace.getDataset(params.datasetName)
                        .then(function (dataset) {
                            if (dataset && dataset.isActive) {
                                return resource.delete(params, data, callback).$promise
                                    .then(function (response) {
                                        return response;
                                    })
                                    .catch(function(e){
                                        if(e.status === 404)
                                            return e;
                                        else
                                            throw e;
                                    });
                            } else {
                                return {
                                    status: 200
                                };
                            }
                        })
                        .then(function (remote) {
                            if(remote.status && (remote.status===200 || remote.status===404))
                                return mevDb.getDataset(params.datasetName)
                                    .then(function (dataset) {
                                        _.remove(dataset[params.dimension].selections, function(selection){
                                            return selection.name===params.selectionName;
                                        });
                                        return mevDb.putDataset(dataset)
                                            .then(function(local){
                                                if(callback)
                                                    callback(remote)
                                                deferred.resolve(remote);
                                                return remote;
                                            });
                                    })
                            else {
                                throw new Error("Failed to delete selection");
                            }
                        })
                        .then(function(response){
                            $rootScope.$broadcast("mui:dataset:selections:deleted", params.dimension, params, response);
                        })
                        .catch(function (e) {
                            console.error("Error fetching selection list: ", params, e);
                            deferred.reject(e);
                            throw e;
                        });
                    return cache;
                };
                return SelectionResource;
            }]);


})
