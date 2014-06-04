define([], function(){
	var ClinicalSummaryRepository = function(dataPromise){
		//private members
		var _data=dataPromise;
		var _fieldNameToIndexMap={};		
		function _init(data){
			console.debug("ClinicalSummaryRepository._init()", data);
			_createFieldNameToIndexMap(data.columns);			
			return data;
		};
		function _createFieldNameToIndexMap(columns){			
			for(var i=0;i<columns.length;i++){				
				_fieldNameToIndexMap[columns[i].name]=i;
			}
		}
		//resolve promise - execution starts here
		var _initPromise=_data.then(_init);
		
		//public methods
		this.getData=function(fields){			
			return _initPromise.then(function(data){
				var results=[];
				//loop rows
				for(var irow=0;irow<data.rows.rows.length;irow++){
					var curRow=data.rows.rows[irow];
					var rowValues=curRow.cells;
					//for every row, get the requested fields
					for(var ifield=0;ifield<fields.length;ifield++){
						var curFieldName=fields[ifield];
						results.push({
							columnId: curFieldName,
							key: rowValues[0].v,
							value: rowValues[_fieldNameToIndexMap[curFieldName]].v
						});
					}
				}	
				return results;
			});
		};
	};
	
	return ClinicalSummaryRepository;
});