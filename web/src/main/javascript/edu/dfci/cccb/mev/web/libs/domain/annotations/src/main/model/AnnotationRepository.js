define([], function(){
	var AnnotationRepository = function($q, AnnotationFieldsResource, AnnotationValuesResource, AnnotationProjectIdResource){

		var current = {};

		return function(dimention){
				//private members		
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
				var _projectIdPromise = AnnotationProjectIdResource.get(dimention).then(function(data){
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
			var _fieldsPromise = AnnotationFieldsResource.get(dimention);
			var _valuesPromise = AnnotationValuesResource.get(dimention);		

			var _initPromise=$q.all({
				columns:  _fieldsPromise,
				rows: _valuesPromise
			});
			_fieldsPromise.then(_init);

			//public methods
			this.getFields=function(){
				var deffered = $q.deffer;
				return _isOld().then(function(isOld){					
					if(isOld){						
						_fieldsPromise = AnnotationFieldsResource.get(dimention);
						_valuesPromise = AnnotationValuesResource.get(dimention);		
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
					var fromIndex = key ? _fieldNameToIndexMap[key] : 6;
					var toIndex = _fieldNameToIndexMap[field];

					var map = {};
					if(typeof data.columns.error!="undefined" )
						throw new Error("Error in AnnotationRepository columns:" + data.columns.error);
					else if(typeof data.rows.error!="undefined")
						throw new Error("Error in AnnotationRepository rows:" + data.rows.error);


					for(var irow=0;irow<data.rows.rows.length;irow++){
						var curRow=data.rows.rows[irow];
						var rowValues=curRow.cells;
						map[rowValues[fromIndex].v] = rowValues[toIndex].v;						
					}

					return map;
				});
			};
		};
	};
		

	AnnotationRepository.$name="mevAnnotationRepository";	
	AnnotationRepository.$inject=["$q", "AnnotationFieldsResource", "AnnotationValuesResource", "AnnotationProjectIdResource"];
	AnnotationRepository.$provider="factory";
	return AnnotationRepository;
});