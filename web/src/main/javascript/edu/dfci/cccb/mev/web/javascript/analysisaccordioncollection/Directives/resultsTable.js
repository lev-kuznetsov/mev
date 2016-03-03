(function(){

  define(["lodash"], function(_){

    return function(module){

      module.directive('resultsTable', ['pathService', 'resultsTableDefaults', "tableResultsFilter", "$timeout", function(paths, defaults, tableResultsFilter, $timeout){
        return {
            restrict : 'E',
            scope : {
            	id: "=",
            	data : "=data",
            	headers : "=headers",
                filters : "=?filters",
                ordering : "@",
                filterCallback : "&onFilter",
                onPaged : "&"
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
            	function notifyResultChange(){
            		scope.$emit("ui:resultsTable:filteredResults", scope.vm.filteredResults);
            		if(scope.filterCallback)
            			scope.filterCallback({filteredResults: scope.vm.filteredResults});
            	}
//scope.renderedData is populated by the 'as renderedData track by $index' at the ng-repeat template
//Tried tapping renderedData when signaling changes in sort/fitlers. 
//However, this does not work if pagination is enabled because only the first page of the results 
//is rendered. We are using it to notify clients of pagination changed event
            	scope.$watchCollection("renderedData", function(newval, oldval){
            		if(newval){            			
            			console.debug("resultsTable watchCollection", newval);
            			scope.$emit("ui:resultsTable:pageChanged", newval);
            			if(scope.onPaged)
            				scope.onPaged({pageResults: newval});
            		}
            	});
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
            	
            	scope.vm={};
            	scope.vm.filteredResults = tableResultsFilter(scope.data, scope.filters);      
            	notifyResultChange();

            	            	
//            	scope.$watch('filters', function(newval, oldval){  
//            	if(!angular.equals(newval, oldval)){
//            		var filters = _.cloneDeep(newval);
//            		console.debug("resultTable $watch", newval, oldval, filters);                		                		
////            		scope.filterCallback({filterParams: filters});
//            	}
//            }, true);
            	            	
            	scope.vm.applyFilter=function($event){
            		 if ($event.which === 13){            			 
            			 console.debug("applyFilter", scope.filters, scope.filterForm);
            			 Object.keys(scope.filters).map(function(key){
            				var filter = scope.filters[key];
            				if(filter.max && !filter.value || filter.value>filter.max)
            					filter.value=filter.max;
            			 });            			
            			 scope.vm.filteredResults = tableResultsFilter(scope.data, scope.filters);
            			 scope.reorderTable({field: scope.tableOrdering});
            			 scope.filterForm.$setPristine();
            		 }
            	};
            	
            	
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
                    scope.vm.filteredResults.sort(function(a, b){
                    	if (a[header.field] > b[header.field]) {
                    	    return 1*ctr;
                    	  }
                    	  if (a[header.field] < b[header.field]) {
                    	    return -1*ctr;
                    	  }
                    	  // a must be equal to b
                    	  return 0;
                    })
                    notifyResultChange();
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
    	    return function (input, fractionSize, header) {
    	        if (isNaN(input)) {
    	            return input;
    	        } else {
                    if(header && header.datatype==="integer") 
                        fractionSize=0;
    	        	if(Math.abs(input)>1000)    	        		 
    	        		return Number.parseFloat(input).toExponential(fractionSize);
    	        	else
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
