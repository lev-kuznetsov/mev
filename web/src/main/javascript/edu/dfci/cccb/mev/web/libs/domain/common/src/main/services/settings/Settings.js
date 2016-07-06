define(["bowser"], function(bowser){"use strict";
   var service = function(){
       this.db = {
           enabled: bowser.chrome && bowser.version > 43 || bowser.firefox
        };
   };
    service.$inject=[];
    service.$provider="service";
    service.$name="mevSettings";
    return service;
});