define([], function(){
	var ClinicalSummaryRepository = function(dataPromise){
		//private members		
		var _fieldNameToIndexMap={};		
		function _createFieldNameToIndexMap(columns){			
			for(var i=0;i<columns.length;i++){				
				_fieldNameToIndexMap[columns[i].name]=i;
			}
		}
		function _init(data){
			_createFieldNameToIndexMap(data.columns);			
			return data;
		};
		
		//init local variables after data promise is resolved
		var _initPromise=dataPromise.then(_init);
		
		//public methods
		this.getData=function(fields){			
			return _initPromise.then(function(data){
				var results=[];
				//at this point the only error is if the Annotations have not been loaded. 
				//so just return an empty array
				if(typeof data.columns.error!="undefined" 
					|| typeof data.rows.error!="undefined")
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
	};
	
	return ClinicalSummaryRepository;
});