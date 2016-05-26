define(['lodash', 'pouchdb', 'blob-util'], function(_, PouchDB, blobUtil){
    return function DatasetValueSourceCache(source){
        var cache = {
            get: function(){
                var db = new PouchDB(source.id);
                return db.getAttachment("values", "all")
                    ["catch"](function(e){
                        if(e.status===404){
                            return source.get().then(function(response){
                                var doc = {
                                    _id: "values",
                                    _attachments: {
                                        "all": {
                                            data: new Blob([response.data]),
                                            type: "application/octet-stream",
                                            content_type : "application/octet-stream"
                                        }
                                    }
                                };
                                db.put(doc)
                                return response;
                            })["catch"](function(e){
                                throw e;
                            });
                        }else{
                            throw e;
                        }
                    }).then(function(values){
                        if(values instanceof Blob)
                            return blobUtil.blobToArrayBuffer(values).then(function(ab){
                                return {
                                    data: ab
                                }
                            })["catch"](function(e){
                                throw e;
                            })
                        else
                            return values;
                    })["catch"](function(e){
                        throw e;
                    });
            }
        };
        _.assign(this, cache);
    };
});