define([], function(){
	
	function exchange(arr, i, j){
		var hold = arr[j];
		arr[j] = arr[i];
		arr[i] = hold;
	}
	
	function randomize(arr){
		for (var i = 0; i< arr.length; i++){
			arr[i]
		}
	}
	
	//selectionSort :: !values([Values]), !sortedValues([Number}), ?Func -> !sortedValues([Number])
	//  Applies selection sort on values array using auxiliary array sortedValues. Sorted values are
	//    determined by specified optional key function keyF that defaults to .value on objects in values
	//    array
	return function(keyF){

		
		var self = this;
		var keyf = function(j){
			return parseFloat(j.value);
		}
		
		if (keyF){
			keyf = keyF;
		}
		
		
		var pivot = 0;
		
		while (pivot < self.values.length){
			
			var leader = {value:Number.POSITIVE_INFINITY, index:undefined, place:undefined};
			
			for (var ind = pivot; ind < self.sortedValues.length; ind++){
                
				if (keyf(self.values[self.sortedValues[ind]]) < leader.value) {
        
					leader.index = ind;
                    leader.value = keyf(self.values[self.sortedValues[ind]]);
                    leader.place = self.sortedValues[ind];
					
				}
			}
            
			self.sortedValues[leader.index] = self.sortedValues[pivot];
			self.sortedValues[pivot] = leader.place;
			pivot++;
		};
	}
})