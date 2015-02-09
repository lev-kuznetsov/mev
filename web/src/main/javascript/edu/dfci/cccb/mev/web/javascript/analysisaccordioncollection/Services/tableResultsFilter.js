(function(){
    
    define([], function(){
        return function(module){
            module.filter('tableResultsFilter', ['$filter', function(){
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
                        filteredArray = 
                            $filter('filter')(filteredArray, thresholder(filterOptions[key]['op'])(filterOptions[key]['value']))
                    }
                    
                    return filteredArray
                }
            }])
        }
    })
    
    function thresholder(op){
        if (op == '>=') { 
            return gte
        } else if (op == '<=') { 
            return lte
        } else {
            return eql
        }
    }
    
    function gte(bound){
        return function(actual){ return (bound ? actual >= bound : true) }
    }
    
    function lte(bound){
        return function(actual){ return (bound ? actual <= bound : true) }
    }
    
    function eql(expected){ 
        return function(actual){ return (bound ? actual == expected : true) } 
    } 
    
})()