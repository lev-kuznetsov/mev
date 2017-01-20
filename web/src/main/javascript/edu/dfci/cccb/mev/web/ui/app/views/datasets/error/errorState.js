define(["./errorModal.tpl.html"], function(template){
   var config = function($stateProvider){
       $stateProvider
       .state("root.datasets.error", {
           parent: "root.datasets",
           params: {
               header: "Unknown Error",
               message: "Please file a bug report",
               error: undefined
           },
           displayName: "Error",
           onEnter: ["$state", "$stateParams", "$uibModal", function ($state, $stateParams, $modal){
               $modal.open({
                   template: template,
                   resolve: {},
                   controller: ["$scope", function(scope){
                       scope.vm = {
                           header: $stateParams.header,
                           message: $stateParams.message,
                           error: $stateParams.message,
                           dismiss: function() {
                               scope.$dismiss();
                           }
                       }

                   }]
               }).result.finally(function(){
                       $state.go("^", {}, {reload: true});
                   });
            }]

       });
   };
   config.$inject=["$stateProvider"];
   config.$provider="config";
   return config;
});