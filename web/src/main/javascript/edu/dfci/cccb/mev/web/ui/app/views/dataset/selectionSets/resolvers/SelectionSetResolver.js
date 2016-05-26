define(["mui"], function(mui){
   var factory = function() {
       var resolver = function SelectionSetResolver($stateParams, dataset, dimension){
           var reset = dataset.resetSelections(dimension);
           console.debug("resolve selectionSet", reset);
           return reset.$promise.then(function(){
               if($stateParams.setId==="new"){
                   var selectionSet1 = {name: "new", type: dimension};
                   console.debug("selectionSet1", selectionSet1);
                   return selectionSet1;
               }


               var selectionSet = _.find(dataset[dimension].selections, function(selection){
                   return selection.name === $stateParams.setId;
               });

               if(!selectionSet){
                   throw new Error("Selection Set '" + $stateParams.setId + "' not found" );
               }

               console.debug("resolved selectionSet", selectionSet);
               return selectionSet;
           });
       }
       resolver.$inject=["$stateParams", "dataset", "dimension"];
       return resolver;
   }
    factory.$name="SelectionSetResolver";
    factory.$provider="factory";
    factory.$inject=[];
    return factory;
});