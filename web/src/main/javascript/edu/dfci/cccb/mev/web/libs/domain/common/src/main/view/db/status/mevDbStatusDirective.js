define([], function(){"use strict";
   var directive = function(mevDb){
       return {
           restrict: "EAC",
           template: "<div>status: {{ vm.getStatus() }}</div>",
           controller: ["mevDb", function(mevDb){
               this.getStatus = mevDb.getStatus;
           }],
           controllerAs: "vm"
       }
   };
    directive.$name="mevDbStatus";
    directive.$provider="directive";
    directive.$inject=["mevDb"];
    return directive;
});