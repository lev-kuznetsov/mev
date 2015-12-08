//importScripts('/library/webjars/requirejs/2.1.10/require.js');
//importScripts('/container/ui/requirejs.config.dev.js');
importScripts("//cdn.jsdelivr.net/pouchdb/5.1.0/pouchdb.min.js");
importScripts("/container/vendor/q/q.js");
var e;
self.chunkSize = 10e6;
self.itemsPerChunk = this.chunkSize / Float64Array.BYTES_PER_ELEMENT;
function chunkName(index){
	return "chunk"+index;
}
addEventListener("message", function(_e){
	//require({baseUrl: "./"}, ["require", ""],
//	require(REQUIREJS_CONFIG, ["require", "PouchDB"],
//	    function(require, PouchDB) {
//	        console.debug("In Worker!", REQUIREJS_CONFIG, PouchDB, self);			
//			postMessage("from worker");
//			db.get("values")
//			["catch"](function(e){
//				console.debug("worker db get error", e);
				e=_e;
				return fetchDataValues(e.data)
				.then(chunkDataValues)
				["catch"](function(e){
					throw e;
				})
				.then(saveDataValues)
				.then(done)
//				["catch"](function(e){
//					throw e;
//				});
//			}).then(function(response){
//				console.debug("worker db response", response);
//				ready(response);
//			});
//	    }
//	);
//	console.debug("worker: message", self, e.data, e.data.id);
//	self.postMessage("Echo: "+e.data);
});

function fetchDataValues(dataset){
	var deferred = Q.defer();
//		$http.get('/dataset/'+dataset.id+'/data/values', {params: {format: "binary"}, responseType: "arraybuffer", headers: {"Accept": "application/octet-stream"}})
	var valuesPromise = deferred.promise
//	.then(function(values){
//		var ab = values.data;     				
////			var dataview = new DataView(ab);
//			console.debug("worker swap: array", ab.byteLength);				   	      				
////			dataset.valuesBuffer = ab;
////			dataset.dataview = dataview;
//			
//			return ab;
//	});

	load('/dataset/'+dataset.id+'/data/values?format=binary', "arraybuffer", "application/octet-stream", function(xhr){
		console.debug("worker, loaded data", xhr);
		deferred.resolve(xhr.response);
	});
	
	return valuesPromise;
}

function chunkDataValues(values){
	var ab = values;
	var chunks = {};
	for (var i = 0, size = 0; size < ab.byteLength; size += self.chunkSize, i++){
//		promise = db.lru.put("values"+i, new Blob([ab.slice(size, size + chunkSize)]), "application/octet-binary");
		chunks[chunkName(i)]={
			type : "application/octet-stream",
			data : new Blob([ab.slice(size, size + self.chunkSize)]),
			content_type : "application/octet-stream"
		};
		console.debug("worker chunk", i);
	};
	return chunks;
}

function saveDataValues(chunks){
	console.debug("worker swap: chunks", chunks);
	var doc = {_id: "values", _attachments: chunks};
	var db = new PouchDB(e.data.id);
//	console.debug("worker worker db", db);
	return db.put(doc)
	["catch"](function(err){
		console.log('worker swap: error put', err);
		throw err;
	});
}

function done(response) {
	console.debug("worker db response", response);
	postMessage("done");
	self.close();
}

//simple XHR request in pure JavaScript
function load(url, responseType, accept, callback) {
	var xhr=undefined;
	if(typeof XMLHttpRequest !== 'undefined'){		
		xhr = new XMLHttpRequest();
	} else {
		var versions = ["MSXML2.XmlHttp.5.0", 
			 	"MSXML2.XmlHttp.4.0",
			 	"MSXML2.XmlHttp.3.0", 
			 	"MSXML2.XmlHttp.2.0",
			 	"Microsoft.XmlHttp"];
		for(var i = 0, len = versions.length; i < len; i++) {
		try {
			xhr = new ActiveXObject(versions[i]);
			break;
		}
			catch(e){}
		} // end for
	}	
	xhr.onreadystatechange = ensureReadiness;
	function ensureReadiness() {
		if(xhr.readyState < 4) {
			return;
		}	
		if(xhr.status !== 200) {
			return;
		}
		// all is well	
		if(xhr.readyState === 4) {
			callback(xhr);
		}			
	}		
	xhr.open('GET', url, true);
	if(responseType){		
		xhr.responseType = responseType;
	}
	xhr.setRequestHeader("Accept", accept);
	xhr.send('');
}