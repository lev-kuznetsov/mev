define(["./sessionTimeoutModal.tpl.html"], function(template){
   var config = function($stateProvider){
       $stateProvider
       .state("root.datasets.sessionTimeout", {
           parent: "root.datasets",
           params: {
               datasetId: undefined
           },
           displayName: "Session Timeout",
           onEnter: ["$state", "$stateParams", "$uibModal", function ($state, $stateParams, $modal){
               $modal.open({
                   template: template,
                   resolve: {},
                   controller: ["$scope", function(scope){
                       scope.dismiss = function() {
                           scope.$dismiss();
                       };
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