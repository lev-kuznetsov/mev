(function(){

  define([], function(){

    return function(module){

      module.directive('resultsTable', ['pathService', 'resultsTableDefaults', function(paths, defaults){
        return {
            restrict : 'E',
            scope : {
            	data : "=data",
            	headers : "=headers",
                filters : "=filters",
                ordering : "@"
            },
            templateUrl : paths.module + '/templates/resultsTable.tpl.html',
            link : function(scope, elem, attrs) {
            	
                //Table reordering methods
                var ctr = -1;
                scope.tableOrdering = attrs.ordering || defaults.getOrdering();
                console.debug("ordering", attrs.ordering, defaults.getOrdering(), scope.tableOrdering);
                scope.reorderTable = function (header) {
                    ctr = ctr * (-1);
                    if (ctr == 1) {
                        scope.tableOrdering = header.field;
                    } else {
                        scope.tableOrdering = "-" + header.field;
                    }
                }
            }	
        };
      }]);
      
      module.provider("resultsTableDefaults", function(){
    	 var defaultOrdering = undefined;
    	 this.setOrdering=function(ordering){
    		 console.debug("set ordering", ordering);
    		 defaultOrdering=ordering;
    	 }
    	 
    	 this.$get=function(){
    		 return {
    			 getOrdering: function(){
    				 return defaultOrdering;
    			 }
    		 }
    	 }
      });
      
      return module;

    }

  })

})()
