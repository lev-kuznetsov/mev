define([], function(){
	var AnnotationRepository = function(
		$rootScope,
		$q,
		AnnotationFieldsResource,
		AnnotationValuesResource,
		AnnotationProjectIdResource,
		AnnotationExportResource,
		mevDb){

		var current = {};

		return function(dimension){
				//private members
			var _self = this;
			var _projectId=false;
			var _fieldNameToIndexMap={};	
			var _columns;	
			function _createFieldNameToIndexMap(columns){	
				_columns=columns;
				for(var i=0;i<columns.length;i++){				
					_fieldNameToIndexMap[columns[i].name]=i;
					columns[i].idx=i;
				}
			}
			function _init(data){
				_createFieldNameToIndexMap(data);	
				console.debug("AnnotationRepository", _fieldNameToIndexMap);
				return data;
			}
			
			function _isOld(){				
				var _projectIdPromise = AnnotationProjectIdResource.get(dimension).then(function(data){
					if(_projectId && _projectId!==data.project){
						return true;
					}else{
						_projectId = data.project;
						return false;
					}
				});	

				return _projectIdPromise;	
			}

			//init local variables after data promise is resolved
			var _fieldsPromise = AnnotationFieldsResource.get(dimension);
			var _valuesPromise = AnnotationValuesResource.get(dimension);		

			var _initPromise=$q.all({
				columns:  _fieldsPromise,
				rows: _valuesPromise
			});
			_fieldsPromise.then(_init);
			function saveAnnotations(project, dimension){
				var datasetId = project.metadata.customMetadata.datasetName;
				dimension = dimension || project.metadata.customMetadata.dimension
					? project.metadata.customMetadata.dimension.toLowerCase()
					: undefined;
				;
				console.debug("loaded column annotations", project, datasetId, dimension);
				return _self.export(datasetId, dimension)
					.then(function(blob){
						return mevDb.putAnnotations(datasetId, dimension, blob);
					})
					.catch(function(e){
						console.error("Error saving annotations: ", datasetId, dimension);
						throw e;
					});
			}
			$rootScope.$on("openRefine:loadedAnnotations:row", function(event, project){
				console.debug("loaded row annotations", project);
				return saveAnnotations(project);
			});
			$rootScope.$on("openRefine:loadedAnnotations:column", function(event, project) {
				console.debug("loaded column annotations", project);
				return saveAnnotations(project);
			});
			//public methods
			this.getFields=function(){
				var deffered = $q.deffer;
				return _isOld().then(function(isOld){					
					if(isOld){
						_fieldsPromise = AnnotationFieldsResource.get(dimension);
						_valuesPromise = AnnotationValuesResource.get(dimension);
						_initPromise=$q.all({
							columns:  _fieldsPromise,
							rows: _valuesPromise
						});						
						_fieldsPromise.then(_init);
					}
					return _fieldsPromise.then(function(){
						return _columns;
					});
				});
			};

			this.getDataKeyVal=function(fields){			
				return _initPromise.then(function(data){
					var results=[];
					//at this point the only error is if the Annotations have not been loaded. 
					//so just return an empty array
					if(typeof data.columns.error!="undefined" || typeof data.rows.error!="undefined")
						return results;
					//loop rows
					for(var irow=0;irow<data.rows.rows.length;irow++){
						var curRow=data.rows.rows[irow];
						var rowValues=curRow.cells;
						//for every row, get the requested fields
						for(var ifield=0;ifield<fields.length;ifield++){
							var curFieldName=fields[ifield];
							var curFieldIndex=_fieldNameToIndexMap[curFieldName];
							//if the requested field does not exist in the dataset, skip it
							if(typeof curFieldIndex !="undefined"){
								results.push({
									columnId: curFieldName,
									key: rowValues[0].v,
									value: rowValues[curFieldIndex].v
								});
							}
						}
					}	
					return results;
				});
			};
			
			this.getDataTable=function(fields){			
				return _initPromise.then(function(data){
					var results=[];
					//at this point the only error is if the Annotations have not been loaded. 
					//so just return an empty array
					if(typeof data.columns.error!="undefined" || typeof data.rows.error!="undefined")
						return results;
					//loop rows
					for(var irow=0;irow<data.rows.rows.length;irow++){
						var curRow=data.rows.rows[irow];
						var rowValues=curRow.cells;
						var row={
							key: rowValues[0].v						
						};
						//for every row, get the requested fields
						for(var ifield=0;ifield<fields.length;ifield++){
							var curFieldName=fields[ifield];
							var curFieldIndex=_fieldNameToIndexMap[curFieldName];
							//if the requested field does not exist in the dataset, skip it
							if(typeof curFieldIndex !="undefined"){							
								row[curFieldName]=rowValues[curFieldIndex].v;
							}
						}
						results.push(row);
					}
					return results;
				});
			};

			this.getMapping = function(field, key){			

				return _initPromise.then(function(data){
					//if key is not supplied - assume the first column is the row id				
					var fromIndex = key ? _fieldNameToIndexMap[key] : 
						(_fieldNameToIndexMap["probeset_id"] ? _fieldNameToIndexMap["probeset_id"] : 6);
					var toIndex = _fieldNameToIndexMap[field];

					var map = {};
					if(typeof data.columns.error!="undefined" )
						throw new Error("Error in AnnotationRepository columns:" + data.columns.error);
					else if(typeof data.rows.error!="undefined")
						throw new Error("Error in AnnotationRepository rows:" + data.rows.error);


					for(var irow=0;irow<data.rows.rows.length;irow++){
						var curRow=data.rows.rows[irow];
						var rowValues=curRow.cells;
						if(rowValues[fromIndex])
							map[rowValues[fromIndex].v] = rowValues[toIndex].v;						
					}

					return map;
				});
			};

			this.export = function(datasetId, dimension){
				return AnnotationExportResource.export(datasetId, dimension)
					.then(function(response){
						console.debug(response);
						return new Blob([response.data], {
							type: 'application/x-gzip'
						});
					})
					.catch(function(e){
						throw e
					});
			};
			this.import = function(datasetId, dimension){
				mevDb.getAnnotations(datasetId, dimension)
					.then(function(blob){
						return AnnotationExportResource.import(datasetId, dimension, blob);
					})
					.catch(function(e){
						console.error("Error importing annotatinos", datasetId, dimension);
						throw e;
					})
			}
			
		};
	};
		

	AnnotationRepository.$name="mevAnnotationRepository";	
	AnnotationRepository.$inject=[
		"$rootScope",
		"$q",
		"AnnotationFieldsResource",
		"AnnotationValuesResource",
		"AnnotationProjectIdResource",
		"AnnotationExportResource",
		"mevDb"];
	AnnotationRepository.$provider="factory";
	return AnnotationRepository;
});