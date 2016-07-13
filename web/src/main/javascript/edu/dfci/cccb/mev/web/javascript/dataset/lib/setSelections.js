define([], function(){

    //setSelections :: String, Array [selection], !selections, !row, !column -> null
    return function(dimension, selections){
    //  Method that takes dimension string and array of selections and sets selections on
    //  dataset.
        var self = this;
        
        self[dimension].selections = selections;
        self.selections[dimension] = selections;
        self.mevDb.putDataset(self);
        return null;
        
    }
})
