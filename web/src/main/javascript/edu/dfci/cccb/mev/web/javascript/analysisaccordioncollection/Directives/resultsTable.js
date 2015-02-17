(function(){

  define([], function(){

    return function(module){

      module.directive('resultsTable', ['pathService', function(paths){
        return {
            restrict : 'E',
            scope : {
            	data : "=data",
            	headers : "=headers",
                filters : "=filters"
            },
            templateUrl : paths.module + '/templates/resultsTable.tpl.html',
            link : function(scope) {
            	
                //Table reordering methods
                var ctr = -1;
                scope.tableOrdering = undefined;
                scope.reorderTable = function (header) {
                    ctr = ctr * (-1);
                    if (ctr == 1) {
                        scope.tableOrdering = header.field;
                    } else {
                        scope.tableOrdering = "-" + header.field;
                    }
                }
            }	
        }
      }])

      return module

    }

  })

})()
