define ([ 'angular', 'd3', "lodash"], function (angular, d3, _) { "use strict";
    angular
    .module ('d3colorBrewer', [])
    .factory ('d3colors', [ "$rootScope", function ($rootScope) {
        var self = this;
        
        var ret = Object.create({            
            current: function(val){
                  if(val) {
                        if(this.$current!==val){
                              this.$current=val;
                              $rootScope.$broadcast("ui:d3colors:change", val);
                        }
                  }else{
                        return this.$current || "Blue,Black,Yellow";    
                  } 
            }
        }, 
        {$current: {writable: true, value: "Blue,Black,Yellow"}});

        _.extend(ret, {
            "Blue,Black,Yellow":{
                  3: ["blue", "black", "yellow"]
            },
            "Red,Black,Green":{
                  3: ['red', 'black', 'green']
            },
            "Red,White,Blue":{
                  3: ['red', 'white', 'blue']
            }
      });
      ret.current.bind(ret);
      return ret;
    }]);
    
});