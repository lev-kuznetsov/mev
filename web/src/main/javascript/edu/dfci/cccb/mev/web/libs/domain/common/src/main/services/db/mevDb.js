define(["lodash", "pouchdb"], function(_, PouchDB){"use strict";
    var service = function mevDbService(mevContext, mevSettings, $q){
        var db = new PouchDB("mev", {adapter: 'worker'});
        function ensureDataset(){
            var dataset = mevContext.get("dataset");
            if(!dataset)
                throw new Error("Could not locate dataset for current context: "  + JSON.stringify(mevContext));
            return dataset;
        }
        function getDataset(datasetId){
            if(mevSettings.db.enabled)
                return db.get(datasetId);
            else{
                var deferred = $q.defer();
                deferred.reject({
                    status: 501,
                    message: "Local db is disabled"
                });
                return deferred.promise;
            }
        }
        function putDataset(dataset){
            return getDataset(dataset.id)
                .catch(function(e){
                    if(e.status === 404)
                        return _.assign(dataset, {
                          _id: dataset.id
                        });
                    else
                        throw e;
                })
                .then(function(doc){
                    dataset._id = dataset.id;
                    dataset._rev = doc._rev;
                    var clean = JSON.parse(JSON.stringify(dataset));
                    clean.$promise = undefined;
                    clean._annotations = undefined;
                    clean.values=[];
                    clean.analyses=[];
                    return db.put(clean);
                })
                .catch(function(e){
                    if(e.status===409)
                        putDataset(dataset);
                    else if(e.status === 501)
                        console.warn("Warning saving dataset locally", e);
                    else{
                        console.error("Error saving dataset locally:", e, dataset);
                        throw e;
                    }
                })
        }
        function getDatasets(){
            // return db.allDocs({startKey: "dataset::", endKey: "dataset::\uFFFF;"});
            if(mevSettings.db.enabled)
                return db.allDocs().then(function(result){
                    return _.uniq(result.rows
                        .filter(function(doc){
                            return doc.id.indexOf("/values64") > -1;
                        }).map(function(doc){
                            return doc.id.split("/")[0];
                        })
                    );
                });
            else
                return $q.when([]);
        }
        function formatDocId(path, datasetId){
            datasetId = datasetId
                ? datasetId
                : ensureDataset().id;
            path = _.isArray(path)
                ? path.join("/")
                : path
            return datasetId + "/" + path;
        }
        function _rejectDisabled(){
            var deferred = $q.defer();
            deferred.reject({
                status: 404,
                message: "Local db is disabled"
            });
            return deferred.promise;
        }
        function getDatasetValues(datasetId){
            if(!mevSettings.db.enabled)
                _rejectDisabled();
            return db.getAttachment(formatDocId("values", datasetId), "all");
        }
        function getDatasetValues64(datasetId){
            if(!mevSettings.db.enabled)
                _rejectDisabled();
            return db.getAttachment(formatDocId("values64", datasetId), "chunk0");
        }
        function putDatasetValues(blob){
            if(!mevSettings.db.enabled)
                return;
            var doc = {
                _id: formatDocId("values"),
                _attachments: {
                    "all": {
                        data: blob,
                        type: "application/octet-stream",
                        content_type : "application/octet-stream"
                    }
                }
            };
            db.put(doc);
        }
        function _removeDocByRow(doc){
            return db.remove(doc.id, doc.value.rev);
            // console.debug("remove", doc);
        }
        function _getAllDatasetDocs(datasetId){
            return db.allDocs({
                    startKey: datasetId,
                    endKey: datasetId+"\uffff"
                })
                .then(function(docs){
                    _.remove(docs.rows, function(row){
                        return row.id !== datasetId && row.id.indexOf(datasetId+"/") !== 0;
                    })
                    return docs;
                });
        }
        function deleteDataset(datasetId){
            return _getAllDatasetDocs(datasetId)
                .then(function(docs){
                    return db.bulkDocs(
                        docs.rows.map(function(row){
                            return {
                                _id: row.id,
                                _rev: row.value.rev,
                                _deleted: true
                            }
                        }));
                });
            // return getDataset(datasetId)
            //     .then(_removeDoc)
            //     .finally(getDatasetValues.bind(datasetId))
            //     .finally(getDatasetValues64.bind(datasetId));
        }
        function getAnalyses(datasetId){
            if(!mevSettings.db.enabled)
                return $q.when([]);

            return db.allDocs()
                .then(function(result){
                    return result.rows.filter(function(doc){
                        return doc.id.indexOf(formatDocId(["analysis"], datasetId))>-1;
                    });
                })
                .then(function(docs){
                    return docs.map(function(doc){
                        return doc.id.replace(formatDocId(["analysis"], datasetId), "").replace("/", "");
                    });
                });
            // db.get(formatDocId(["analysis"], datasetId))
            //     .then(function(docs){
            //         return docs.map(function(doc){
            //             return doc.name;
            //         });
            //     });
        }
        function getAnalysis(datasetId, analysisId){
            if(!mevSettings.db.enabled)
                return _rejectDisabled();
            return db.get(formatDocId(["analysis", analysisId], datasetId));
        }
        function putAnalysis(datasetId, analysis){
            if(!mevSettings.db.enabled)
                return _rejectDisabled();

            return getAnalysis(datasetId, analysis.name)
                .catch(function(e){
                    if(e.status === 404)
                        return _.assign(analysis, {
                            _id: formatDocId(["analysis", analysis.name], datasetId)
                        });
                    else
                        throw new Error("Error putting analysis" + JSON.stringify(e));
                })
                .then(function(doc){
                    analysis._rev = doc._rev;
                    return db.put(JSON.parse(JSON.stringify(analysis)));
                })
                .catch(function(e){
                    if(e.status===409)
                        putAnalysis(datasetId, analysis);
                    else{
                        console.error("Error saving analysis locally:" , datasetId, analysis, e);
                        throw e;
                    }
                });

        }
        function deleteAnalysis(datasetId, analysisName){
            return getAnalysis(datasetId, analysisName)
                .then(function(doc){
                    return db.remove(doc);
                });
        }


        function putAnnotations(datasetId, dimension, blob){
            if(!mevSettings.db.enabled)
                return;

            var doc = {
                _id: formatDocId(["annotations", dimension], datasetId),
                _attachments: {
                    "all": {
                        data: blob,
                        type: "application/octet-stream",
                        content_type : "application/octet-stream"
                    }
                }
            };
            db.put(doc)
                .catch(function(e){
                    if(e.status===409)
                        putAnnotations(datasetId, dimension, blob);
                    else
                        throw e;
                });
        }
        function getAnnotations(datasetId, dimension){
            return db.getAttachment(formatDocId(["annotations", dimension], datasetId), "all");
        }
        return {
            getDataset: getDataset,
            putDataset: putDataset,
            deleteDataset: deleteDataset,
            getDatasets: getDatasets,
            getDatasetValues: getDatasetValues,
            putDatasetValues: putDatasetValues,
            getDatasetValues64: getDatasetValues64,
            getAnalysis: getAnalysis,
            putAnalysis: putAnalysis,
            getAnalyses: getAnalyses,
            deleteAnalysis: deleteAnalysis,
            putAnnotations: putAnnotations,
            getAnnotations: getAnnotations
        }
    };

    service.$name="mevDb";
    service.$provider="service";
    service.$inject=["mevContext", "mevSettings", "$q"];
    return service;
});