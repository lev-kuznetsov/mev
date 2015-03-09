define ([ 'angular', 'spec/htmlUnitPolyfill.js'], function (angular) {
  
  describe ("hello", function () {
    
    it ("should say hello", function () {
      expect ("hello").toBe ("hello");
    });
    
    it ("fails when run with the maven-jasmine-plugin", function () {
    	var add = function(x, y){
    		return x+y;
    	};
    	var add2=add.bind(this, 2);
    	expect(add2(3)).toBe(5);
      });
    
  });
  
});