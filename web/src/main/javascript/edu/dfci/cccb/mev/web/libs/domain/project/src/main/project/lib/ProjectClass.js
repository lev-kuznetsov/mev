define([], function(){

    
    //ProjectFactory :: null -> $Function [Project]
    //  Constructor function that builds
    //  project object.
    return function(dataset, generateView) {

        //properties
        
        this.dataset = dataset;
        this.generateView = generateView;
        this.views = [];
        
    };

})