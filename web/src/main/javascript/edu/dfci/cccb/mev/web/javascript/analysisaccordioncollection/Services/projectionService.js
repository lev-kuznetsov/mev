(function(){
    
    define([], function(){
        return function(module){
            module.factory('projectionService', [function(){
                
                //Service for projection operators for analysis results
                
                return {
                    'ids': function(row){ return row.id }
                }
            }])
        }
    })
    
})()