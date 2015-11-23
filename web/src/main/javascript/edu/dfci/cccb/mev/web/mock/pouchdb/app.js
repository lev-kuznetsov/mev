(function() {

	var db = new PouchDB('db1');
	db.initLru(5000000);
	
	var remoteCouch = false;

	// console.debug("db", db);

	var newTodoDom = document.getElementById('new-todo');
	var addTodoDom = document.getElementById('add-todo');
	var fileImageDom = document.getElementById("imageFile");
	var fileDataDom = document.getElementById("dataFile");

	function addTodo(text) {
		var image = fileImageDom.files[0]; // file is a Blob
		var file = fileDataDom.files[0]; // file is a Blob
		console.debug("image", image);
		console.debug("file", file);

		var attachments = {};
		if (image) {
			attachments.image = {
				type : image.type,
				data : image,
				content_type : image.type
			};
		}
		if (file) {
			attachments.dataset = {
				type : "application/octet-binary",
				data : file,
				content_type : "application/octet-binary"
			}
		}

		var todo = {
			_id : new Date().toISOString(),
			title : text,
			completed : false,
			_attachments : attachments
		};

		db.put(todo, function callback(err, result) {		
			if (!err) {
				console.log('Successfully posted a todo!');
			} else {
				console.log('Error put', err);
			}
		});
	}

	// Given an object representing a todo, this will create a list item
	// to display it.
	function createTodoListItem(todo) {
		var checkbox = document.createElement('input');
		checkbox.className = 'toggle';
		checkbox.type = 'checkbox';
		// checkbox.addEventListener('change', checkboxChanged.bind(this,
		// todo));

		var label = document.createElement('label');
		label.appendChild(document.createTextNode(todo.title));
		// label.addEventListener('dblclick', todoDblClicked.todosbind(this, todo));

		var deleteLink = document.createElement('button');
		deleteLink.className = 'destroy';
		// deleteLink.addEventListener('click', deleteButtonPressed.bind(this,
		// todo));

		var divDisplay = document.createElement('div');
		divDisplay.className = 'view';
		// divDisplay.appendChild(checkbox);
		divDisplay.appendChild(label);
		// divDisplay.appendChild(deleteLink);

		// var img = document.createElement('img');
		// // var arrayBufferView = new Uint8Array( this.response );
		// db.getAttachment(todo._id, "image").then(function(blob){
		// var reader = new FileReader();
		// reader.onload = function(e){
		// img.src = e.target.result;
		// img.width = 100;
		// img.height = 100;
		// divDisplay.appendChild(img);
		// };
		// reader.readAsDataURL(blob);
		// })["catch"](function(e){
		// divDisplay.appendChild(document.createElement("span").appendChild(document.createTextNode("
		// - no image")))
		// });

		db.getAttachment(todo._id, "dataset").then(
				function(blob) {
					console.debug("dataset", blob);
					divDisplay.appendChild(document.createElement("span")
							.appendChild(
									document.createTextNode(" data: "
											+ blob.size + " " + blob.type)))
				})["catch"](function(e) {
			divDisplay.appendChild(document.createElement("span").appendChild(
					document.createTextNode(" - no data")))
		});

		var inputEditTodo = document.createElement('input');
		inputEditTodo.id = 'input_' + todo._id;
		inputEditTodo.className = 'edit';
		inputEditTodo.value = todo.title;
		// inputEditTodo.addEventListener('keypress', todoKeyPressed.bind(this,
		// todo));
		// inputEditTodo.addEventListener('blur', todoBlurred.bind(this, todo));

		var li = document.createElement('li');
		li.id = 'li_' + todo._id;
		li.appendChild(divDisplay);
		// li.appendChild(inputEditTodo);

		if (todo.completed) {
			li.className += 'complete';
			checkbox.checked = true;
		}

		return li;
	}

	function redrawTodosUI(todos) {
		var ul = document.getElementById('todo-list');
		ul.innerHTML = '';
		console.debug("redraw");
		todos.forEach(function(todo) {
			console.debug("todo", todo);
			ul.appendChild(createTodoListItem(todo.doc));
		});
	}

	function showTodos() {
		db.allDocs({
			include_docs : true,
			descending : true
		}, function(err, doc) {
			redrawTodosUI(doc.rows);
		});
	}

	function addTodoClickHandler(event) {
		addTodo(newTodoDom.value);
		newTodoDom.value = '';
	}

	var getFileDOM = document.getElementById("get-file");
	function getFile(){
		var xhr = new XMLHttpRequest();
		xhr.open('GET', 'datasetValues.matrix.brca', true);
		xhr.responseType = 'blob';
		
		var ab = undefined;
		var ar = undefined;
		xhr.onload = function(e) {
		  if (this.status == 200) {
		    // Note: .response instead of .responseText
		    var blob = new Blob([this.response], {type: 'application/octet-binary'});
		    console.debug("get blob", blob);		    
		    blobUtil.blobToArrayBuffer(blob).then(function(arrayBuff){
		    	ab = arrayBuff;
		    	ar = new Float64Array(ab, 0);
		    	var dataview = new DataView(ab);
//		    	
		    	console.debug("array", ar.length); // 0);
		    	for(i=0;i<ar.length;i++){
		    		
		    		console.debug("ar" + i, ar[i], dataview.getFloat64(i*Float64Array.BYTES_PER_ELEMENT, false));
		    	}
		    	
		    });
		  }else{
			  console.debug("error on download", this.status);
		  }promises
		};
		console.debug("get-file start");
		xhr.send();

	}
	
	function getFileAB(){
		var xhr = new XMLHttpRequest();
		xhr.open('GET', 'brca.matrix', true);
		xhr.responseType = 'arraybuffer';
		
		var ab = undefined;
		var ar = undefined;
		xhr.onload = function(e) {
			if (this.status == 200) {
				ab = this.response;
				ar = new Float64Array(ab, 0);
				var dataview = new DataView(ab);
				console.debug("array", ar.length, ab.byteLength); // 0);
				for(i=0;i<Math.min(ar.length, 12);i++){					
					console.debug("ar" + i, ar[i], dataview.getFloat64(i*Float64Array.BYTES_PER_ELEMENT, false));
				}				
				
				var promises = [];
				var chunks = {}								
				var chunkSize = 2.5e6 * Float64Array.BYTES_PER_ELEMENT;
				for (var i = 0, size = 0; size < ab.byteLength; size += chunkSize, i++){
//					promise = db.lru.put("values"+i, new Blob([ab.slice(size, size + chunkSize)]), "application/octet-binary");
					chunks["chunk"+i]={
						type : "application/octet-binary",
						data : new Blob([ab.slice(size, size + chunkSize)]),
						content_type : "application/octet-binary"
					};
					console.debug("chunk", i);
				};
				console.debug("chunks", chunks);
				
				db.put({_id : "swap",_attachments : chunks})
				.then(function(response) {
						console.log('Successfully posted a todo!', response);
					})["catch"](function(err){
						console.log('Error put', err);
					})
				.then(function(){
					console.debug("all done");
					db.getAttachment("swap", "chunk0").then(function(chunk){
						console.debug("get chunk", chunk.size, chunk.size);														
					})["catch"](function (err) {
					  console.log("get error", err);
					});
				});
				
//				for (var i = 0, size = 0; size < ab.byteLength; size += chunkSize, i++){
//					promise = db.lru.put("values"+i, new Blob([ab.slice(size, size + chunkSize)]), "application/octet-binary");
//					promises.push(promise);
//					console.debug("cache", i);
//				};
//				
//				console.debug("promises", promises);
//				Q.all(promises).then(function(){
//					console.debug("all done");
//					db.lru.get("value0").then(function(blob){
//						console.debug("get blob", blob.size, blob.size);
//					})["catch"](function (err) {
//						console.log("get error", err);
//					});
//				});
				
				
			}else{
				console.debug("error on download", this.status);
			}
			
		};
		console.debug("get-file start 2");
		xhr.send();
		
	}
	
	getChunkDom = document.getElementById("get-chunk");	
	function getChunkHandler(){
		getChunk(newTodoDom.value);
	}
	window.workingSet = [];
	function getChunk(id){
		console.debug("getting chunk..", id);
		db.getAttachment("swap", "chunk"+id).then(function(blob){			
			
			blobUtil.blobToArrayBuffer(blob).then(function(arrayBuff){
		    	ab = arrayBuff;		    	
		    	var dataview = new DataView(ab);	    	
		    	for(i=0;i<3;i++){		    		
		    		console.debug("ar" + i, dataview.getFloat64(i*Float64Array.BYTES_PER_ELEMENT, false));
		    	}
		    	window.workingSet.push(dataview);
		    	console.debug("get chunk", dataview, window.workingSet.length);
		    });
		})["catch"](function (err) {
		  console.log("get error", err);
		});
	}
	
	function addEventListeners() {
		// newTodoDom.addEventListener('keypress', newTodoKeyPressHandler,
		// false);
		getChunkDom.addEventListener("click", getChunkHandler, false)
		getFileDOM.addEventListener("click", getFileAB, false)
		addTodoDom.addEventListener('click', addTodoClickHandler, false);
		
	}
	
	addEventListeners();
	showTodos();
})();
