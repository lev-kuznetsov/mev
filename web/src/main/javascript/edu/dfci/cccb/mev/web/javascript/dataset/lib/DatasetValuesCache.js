define(['lodash', 'pouchdb', 'blob-util', 'mev-domain-common'], function(_, PouchDB, blobUtil){
    return function DatasetValueSourceCache(source, mevDb){
        var cache = {
            get: function(){
                // var db = new PouchDB(source.id,  {adapter: 'worker'});
                return mevDb.getDatasetValues(source.id)
                    ["catch"](function(e){
                        if(e.status===404 || e.status === 501){
                            return source.get().then(function(response){
                                setTimeout(function(){
                                    mevDb.putDatasetValues(new Blob([response.data]), source.id);
                                    // db.put(doc);
                                }, 10e3);
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