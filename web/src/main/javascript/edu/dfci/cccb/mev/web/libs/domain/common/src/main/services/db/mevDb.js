define(["lodash", "pouchdb"], function(_, PouchDB){"use strict";
    var service = function mevDbService(mevContext){
        var db = new PouchDB("mev", {adapter: 'worker'});
        function ensureDataset(){
            var dataset = mevContext.get("dataset");
            if(!dataset)
                throw new Error("Could not locate dataset for current context: "  + JSON.stringify(mevContext));
            return dataset;
        }
        function getDataset(datasetId){
            return db.get(datasetId);
        }
        function putDataset(dataset){
            return getDataset(dataset.id)
                .catch(function(e){
                    if(e.status === 404)
                        return _.assign(dataset, {
                          _id: dataset.id
                        });
                    else
                        throw new Error("Error updating db" + JSON.stringify(e));
                })
                .then(function(doc){
                    dataset._rev = doc._rev;
                    dataset.$promise = undefined;
                    return db.put(JSON.parse(JSON.stringify(dataset)));
                })
                .catch(function(e){
                    console.error("Error saving dataset locally:" + JSON.stringify(dataset), e)
                    throw new Error("Error saving dataset locally:" + JSON.stringify(e));
                })
        }
        function getDatasets(){
            // return db.allDocs({startKey: "dataset::", endKey: "dataset::\uFFFF;"});
            return db.allDocs().then(function(result){
                return _.uniq(result.rows.map(function(doc){
                    return doc.id.split("/")[0];
                }));
            });
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
        function getDatasetValues(datasetId){
            return db.getAttachment(formatDocId("values", datasetId), "all");
        }
        function getDatasetValues64(datasetId){
            return db.getAttachment(formatDocId("values64", datasetId), "all");
        }
        function putDatasetValues(blob){
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

        return {
            getDataset: getDataset,
            putDataset: putDataset,
            getDatasets: getDatasets,
            getDatasetValues: getDatasetValues,
            putDatasetValues: putDatasetValues,
            getDatasetValues64: getDatasetValues64,
        }
    };

    service.$name="mevDb";
    service.$provider="service";
    service.$inject=["mevContext"];
    return service;
});