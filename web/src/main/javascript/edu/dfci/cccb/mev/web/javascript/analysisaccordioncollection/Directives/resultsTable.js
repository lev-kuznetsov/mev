(function(){

  define(["lodash"], function(_){

    return function(module){

      module.directive('resultsTable', ['pathService', 'resultsTableDefaults', function(paths, defaults){
        return {
            restrict : 'E',
            scope : {
            	data : "=data",
            	headers : "=headers",
                filters : "=?filters",
                ordering : "@",
                filterCallback : "&onFilter" 
            },
            templateUrl : paths.module + '/templates/resultsTable.tpl.html',
            link : function(scope, elem, attrs) {
            	
            	function getOpFromHeader(header){
            		if(header.icon==='search')
            			return "~=";                        		
            		if(angular.isArray(header.icon))
            			return header.icon[0];
            		else
            			return header.icon;                        			
            	}
            	
            	if(!scope.filters){
            		scope.filters={};
                	scope.headers.map(function(header){
                		if(header.icon && header.icon != 'none'){
                			scope.filters[header.field] = {
                					field: header.field,
                					value: header["default"],
                					op: header.op || getOpFromHeader(header),
                					max: header.max
                			};
                		}
                	});
            	}
            	scope.filterCallback({filterParams: scope.filters});

            	var vm={};            	
            	            	
//            	scope.$watch('filters', function(newval, oldval){  
//            	if(!angular.equals(newval, oldval)){
//            		var filters = _.cloneDeep(newval);
//            		console.debug("resultTable $watch", newval, oldval, filters);                		                		
////            		scope.filterCallback({filterParams: filters});
//            	}
//            }, true);
            	            	
            	vm.applyFilter=function($event){
            		 if ($event.which === 13){            			 
            			 console.debug("applyFilter", scope.filters, scope.filterForm);
            			 Object.keys(scope.filters).map(function(key){
            				var filter = scope.filters[key];
            				if(filter.max && !filter.value || filter.value>filter.max)
            					filter.value=filter.max;
            			 });            			
            			 scope.filterCallback({filterParams: scope.filters});            			 
            			 scope.filterForm.$setPristine();
            		 }
            	};
            	scope.vm=vm;
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
                };                
                
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
      
      module.filter('textOrNumber', function ($filter) {
    	    return function (input, fractionSize) {
    	        if (isNaN(input)) {
    	            return input;
    	        } else {
    	            return $filter('number')(input, fractionSize);
    	        };
    	    };
    	});
      
      module.filter('isArray', function() {
    	  return function (input) {
		    return angular.isArray(input);
		  };
      });
      return module;

    }

  })

})()
