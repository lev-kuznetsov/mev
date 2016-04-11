define([], function(){"use strict";
    return function (newval){
                        
        var self = this;
        
        if ( newval.type == "Hierarchical Clustering") {

            self.drawTree(self.DOM.sidePanel,newval.result.row,'vertical');

        } else if (newval.type == "K-means Clustering") {

            
            self.drawCluster(newval, self.DOM.sidePanel);
        }
    };
});