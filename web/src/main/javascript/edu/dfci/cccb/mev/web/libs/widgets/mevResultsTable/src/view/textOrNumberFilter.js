define([], function($filter){ "use strict";
    function mevTextOrNumber($filter) {
        return function(input, fractionSize, header){
            if (isNaN(input)) {
                return input;
            } else {            
                if(header && header.datatype==="integer") 
                        fractionSize=0;
                if(Math.abs(input)>1000)                         
                    return Number.parseFloat(input).toExponential(fractionSize);
                else if(Math.abs(input)<0.001 && input !== 0)
                    return Number.parseFloat(input).toExponential(fractionSize);
                else
                    return $filter('number')(input, fractionSize);
            }    
        };        
    }
    mevTextOrNumber.$inject=["$filter"];    
    mevTextOrNumber.$name="mevTextOrNumber";
    mevTextOrNumber.$provider="filter";
    return mevTextOrNumber;
});