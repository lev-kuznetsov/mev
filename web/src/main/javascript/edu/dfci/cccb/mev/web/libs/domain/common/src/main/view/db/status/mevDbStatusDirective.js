define([], function(){"use strict";
   var directive = function(mevDb, mevContext, $interval, $q, $rootScope){
       return {
           restrict: "EAC",
           template: "<div ng-if=\"vm.dataset\">status: {{ vm.showActive() }} ({{ vm.getStatus() }})</div>"
           + "<div ng-if=\"!vm.dataset\">status: {{ vm.getStatus() }}</div>",
           controller: ["$scope", "mevDb", function(scope, mevDb){
               var _self = this;
               _self.getStatus = mevDb.getStatus;
               _self._isActive = false;
               _self.showActive = function(){
                   return _self._isActive
                       ? "active"
                       : "offline";
               }

               function _setActive(isActive){
                   _self._isActive = isActive ? true : false;
               };
               function _checkActive(){
                   var dataset  = mevContext.get("dataset");
                   return (
                       dataset
                           ? dataset.getIsActive()
                           : $q.when(false)
                   ).then(_setActive);
               }

               //init
               _checkActive();
               var intervalPromise = $interval(_checkActive, 11*60e3);
               var contextListener = $rootScope.$watch(mevContext.get.bind(mevContext, "dataset"), function(newVal, oldVal){
                   _self.dataset = newVal;
                   _checkActive();
               });
               //cleanup
               scope.$on('$destroy', function() {
                   $interval.cancel(intervalPromise);
                   contextListener();
               });
           }],
           controllerAs: "vm"
       }
   };
    directive.$name="mevDbStatus";
    directive.$provider="directive";
    directive.$inject=["mevDb", "mevContext", "$interval", "$q", "$rootScope"];
    return directive;
});