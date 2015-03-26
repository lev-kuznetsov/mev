(function(){
    
    define([], function(){
        return function(module){
        	
            module.filter('tableResults', ['$filter', 'compareFactory', function($filter, compare){
                return function(array, filterOptions){
                    //function to filter array with parameters specified by filterOptions
                    //  where filterOptions is an object with properties 
                    //  {'<columnName>':{id:<arrayPropertyName>, op:<stringOperator>, value:<filterParameter>}}
                    //     arrayPropertyName is the key to filter on in the array, stringOperator is ">=" | "<=" | "=="
                    //     and filterParameter is object to check array value at property id against using op
                    var filteredArray = array

                    var filteredColumn = Object.keys(filterOptions)
                    
                    for (filteredColumnIndex in filteredColumn){
                        var key = filteredColumn[filteredColumnIndex]
                        
                        var comparator = compare()
                        	.op(filterOptions[key]['op'])
                        	.key(key)
                        	.bound(filterOptions[key]['value'])
                        	
                        //Case for numeric comparisons
                        if (filterOptions[key]['op'] == '>=' | filterOptions[key]['op'] == '<=' ){
                        	comparator.transformer(parseFloat)
                        }
                        
                        //Case for string contains comparisons
                        if (filterOptions[key]['op'] == '~='){
                        	comparator.transformer(String.prototype.toLowerCase)
                        }
                        
                        filteredArray = 
                            $filter('filter')(filteredArray, comparator)
                    }
                    
                    return filteredArray
                }
            }])
        }
    })
    
}())