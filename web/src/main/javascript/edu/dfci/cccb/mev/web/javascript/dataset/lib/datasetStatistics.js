define([], function(){
	return function(){
		var self = this;
		
		return {
			percentile : function(percent){
				
				var f = Math.floor( (self.sortedValues.length-1) * .01 * parseFloat(percent) ),
					c = Math.ceil( (self.sortedValues.length-1) * .01 * parseFloat(percent) );

				return (self.values[self.sortedValues[f]].value + self.values[self.sortedValues[c]].value ) / 2
			},
			max : function(){
				return this.percentile(100)
			},
			min : function(){
				return this.percentile(0)
			},
			median : function(){
				return this.percentile(50)
			},
			quartile : function(q){
				return this.percentile(q*25)
			}
			
		}
		
	}
});