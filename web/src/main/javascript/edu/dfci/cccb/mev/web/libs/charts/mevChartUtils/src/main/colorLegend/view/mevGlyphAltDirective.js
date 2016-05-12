define(["lodash"], function(_){
   var directive = function($timeout){
       return {
           restrict: "A",
           scope: {
               config: "@mevGlyphAlt"
           },
           link: function(scope, elm, attr){
               var domElm = elm.get(0);
               scope.glyphAlt = {
                   width: function(){
                       return domElm.offsetWidth;
                   },
                   height: function(){
                       return domElm.offsetHeight;
                   },
                   hasDims: function(){
                       return this.height()>0 && this.width()>0;
                   },
                   isInView: function(){
                       return domElm.offsetParent!==null;
                   }
               };
               scope.$watch(scope.glyphAlt.isInView.bind(scope.glyphAlt), function(newVal, oldVal){
                   if(newVal===true)
                       if(!scope.glyphAlt.hasDims())
                           elm.text(scope.config);

               });

           }
       };
   };
   directive.$name="mevGlyphAlt";
   directive.$provider="directive";
   directive.$inject=["$timeout"];
   return directive;
});