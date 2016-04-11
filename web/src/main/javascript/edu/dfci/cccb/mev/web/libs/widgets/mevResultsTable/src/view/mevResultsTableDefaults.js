"use strict";
define([], function(){
	function mevResultsTableDefaults(){
    	 var defaultOrdering;
    	 this.setOrdering=function(ordering){
    		 console.debug("set ordering", ordering);
    		 defaultOrdering=ordering;
    	 };
    	 
    	 this.$get=function(){
    		 return {
    			 getOrdering: function(){
    				 return defaultOrdering;
    			 }
    		 };
    	 };
	}
	mevResultsTableDefaults.$inject=[];
	mevResultsTableDefaults.$name="mevResultsTableDefaults";
	mevResultsTableDefaults.$provider="provider";
	return mevResultsTableDefaults;
});