"use strict";
define([], function($filter){
    function mevTextOrNumber(input, fractionSize) {
        if (isNaN(input)) {
            return input;
        } else {
        	if(Math.abs(input)>1000)    	        		 
        		return Number.parseFloat(input).toExponential(fractionSize);
        	else
        		return $filter('number')(input, fractionSize);
        }
    }
    mevTextOrNumber.$inject=["$filter"];    
    mevTextOrNumber.$name="mevTextOrNumber";
    mevTextOrNumber.$provider="filter";
    return mevTextOrNumber;
});