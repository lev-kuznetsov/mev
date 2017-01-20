define(["steal-jasmine", "pouchdb", "q"], function(jasmineRequire, PouchDB, $q){"use strict";
    return describe("Test PouchDB Size Limit", function(){
        beforeEach(module("test"));
        var db;
        function _getDb(){
            return new PouchDB("mev-test-db-size-limit", {auto_compaction: true});
        }
        function _initDb(){
            db = _getDb();
        }
        _initDb();

        function _getAllDocs(options){
            return options
                    ? db.allDocs(options)
                    : db.allDocs();
        }
        function _deleteAll(docs){
            console.log("deleting docs " + docs.rows.length);
            return db.bulkDocs(
                docs.rows.map(function(row){
                    return {
                        _id: row.id,
                        _rev: row.value.rev,
                        _deleted: true
                    }
                }));
        }
        function _createDoc(arraySize, id){
            var ab = new Float64Array(arraySize);
            for(var j=0;j<ab.length;j++)
                ab[j] = Math.random();
            var blob = new Blob([ab], {type: "application/octet-stream"})
            return {
                _id: "doc"+id,
                _attachments: {
                    x: {
                        data: blob,
                        type: "application/octet-stream",
                        content_type : "application/octet-stream"
                    }
                }
            }
        }
        function _writeBlob(doc){
            return db.put(doc)
                .then(function(doc){
                    if(window.gc){
                        console.log("gc", doc.id, doc.ok);
                        window.gc();
                    }else
                        console.log("wrote", doc.id, doc.ok);
                })
                .catch(function(e){
                    if(e.status === 409)
                        db.put(doc);
                    else
                        throw e;
                })
        }
        function _writeBlobs(totalSize, blobSize){
            // var totalSize = 1e6 //1MB
            // var blobSize = 1e3; //1KB
            var numOfBlobs = Math.floor(totalSize/blobSize);
            var promises = [];
            console.log("writing ... ", totalSize, blobSize, numOfBlobs);
            var arraySize = Math.floor(blobSize/Float64Array.BYTES_PER_ELEMENT);
            var promiseChain;
            for(var i=0;i<numOfBlobs;i++){
                if(!promiseChain){
                    promiseChain = _writeBlob(_createDoc(arraySize, i))
                }else{
                    promiseChain = promiseChain.then(function(i){
                        return _writeBlob(_createDoc(arraySize, i))
                    }.bind(null, i));
                }
            }
            return promiseChain
                .then(function(){
                    console.log("Done!");
                });
        }
        function _cleanup(){
            return _getAllDocs()
                .then(_deleteAll);
                // .then(function(){
                //     return db.compact();
                // });
        }
        var originalTimeout;
        beforeEach(function(done){
            originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
            jasmine.DEFAULT_TIMEOUT_INTERVAL = 60*60e3;
           _cleanup()
               .then(done)
               .catch(function(e){
                   console.log("Error in cleanup", e);
                   if(e.reason === "QuotaExceededError"){
                        return db.destroy()
                            .catch(function(e){
                                console.error(e);
                                throw e;
                            })
                            .then(_initDb)
                            .catch(function(e){
                                console.error(e);
                                throw e;
                            })
                   }else{
                       throw e;
                   }
               });
        });
        afterEach(function(){
            jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
        });
        it("writes MBs", function(done){
            var totalSize = 1e6;
            var blobSize = 100e3;
            var numOfBlobs = Math.floor(totalSize/blobSize);
            _writeBlobs(totalSize, blobSize)
                .then(function(){
                    return _getAllDocs({
                        include_docs: true,
                        attachments: true,
                        binary: true
                    });
                })
                .then(function(docs){
                    expect(docs.rows.length).toBe(numOfBlobs);
                    console.log("Number of docs checks out");
                    return docs;
                })
                .then(done)
                .catch(function(e){
                    console.error("Error: ", e);
                    done();
                    throw e;
                })
            // $rootScope.$apply(function(){
            //     $state.go("root.dataset.analysis");
            // });
            // console.log("$state.current", $state.current);
            // expect($state.current.name).toBe("root.dataset.analysis");
            // expect(mevContext.root()).toBe(project);
            // expect(mevSelectionLocator.find("column")).toBe(project.dataset.selections.column);
            // expect(mevSelectionLocator.find("column").length).toBe(2);
            // expect(mevSelectionLocator.find("column")[0].name).toBe("s2");
        });
    });
});
