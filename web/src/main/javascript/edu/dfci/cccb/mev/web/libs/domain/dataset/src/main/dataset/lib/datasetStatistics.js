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
			},
			contingency : function(experiment){

				//Check for required experiment attributes
				if(!experiment.groups || 
						!experiment.dimension || 
						(experiment.threshold == undefined) || 
						!experiment.population){
					throw new TypeError('Missing experment attribute.' +
							' Groups: '+ experiment.groups + 
							' Dimension: '+ experiment.dimension +
							' Threshold: '+ experiment.threshold )

					return
				}

				//Check to make sure there are only 2 groups
				if (experiment.groups.length > 2 || experiment.groups.length < 2){
					throw new RangeError('Group length incorrect. Expected: 2. Received: ' + 
							experiment.groups.length)

					return

				}

				//Check for group with no elements
				for(group in experiment.groups){
					if (experiment.groups[group].length < 1){
						throw new RangeError('Group with no elements found! Requires at least one element.')
					}
				}

				//Check for intersection
				for(element in experiment.groups[0]){
					if ( experiment.groups[1].indexOf(experiment.groups[0][element]) >= 0 ) {
						throw new TypeError('Intersection of groups is not null. Element in both groups: ' +
								experiment.groups[0][element] )
					}
				}

				//create contingency table


				var table = [{above:0,below:0}, {above:0,below:0}]

				//For each group
				for(group in experiment.groups){

					//For each member
					for(member in experiment.groups[group]){

						//if population is a column
						if (experiment.dimension == 'column'){

							//if member's value is above threshold
							if (self.get([experiment.groups[group][member], experiment.population])
									.value >= experiment.threshold){
								//add to above
								table[group].above++ 

							} else {
								table[group].below++ 
							}


							//else add to below

						}


						//if population is a row
						if (experiment.dimension == 'row'){


							//if member's value is above threshold
							if (self.get([experiment.population, experiment.groups[group][member]])
									.value >= experiment.threshold){
								//add to above
								table[group].above++ 

							} else {
								table[group].below++ 
							}


							//else add to below

						}


					}



				} //End of table build

				return table



			}
			
		}
		
	}
});