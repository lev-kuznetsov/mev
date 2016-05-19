define(["./sessionTimeoutModal.tpl.html"], function(template){
    var config = function($stateProvider){
        $stateProvider
            .state("root.dataset.home.sessionTimeout", {
                parent: "root.dataset.home",
                displayName: "Session Timeout",
                onEnter: ["$state", "$stateParams", "$uibModal", "dataset", function ($state, $stateParams, $modal, dataset){
                    $modal.open({
                        template: template,
                        resolve: {},
                        controller: ["$scope", function(scope){
                            scope.stay = function() {
                                scope.$dismiss();
                            };
                            scope.leave = function() {
                                scope.$close(true);
                            };
                        }]
                    }).result.then(function(){
                            $state.go("root.datasets");
                        })
                }]

            });
    };
    config.$inject=["$stateProvider"];
    config.$provider="config";
    return config;
});