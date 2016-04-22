define(["lodash"], function(_){
	return {
		selectionIntersect: function(params){
		// type SelectionIntersect :: Params => Selection
		//
		// type Params :: (Selection1, Selection2, Dimension)
		// type Selection1 :: String
		// type Selection2 :: String
		// type Selection3 :: String
		//
		// type Selection :: {String | Null}
			
		//   Function to check a pair of selections on this and return
		//   keys that intersect in both

			var self = this
			
			var set1, set2
			self.selections[params.dimension].map(function(selection){
				if (selection.name == params.selection1){
					set1 = selection
				}
				if (selection.name == params.selection2){
					set2 = selection
				}
			})
			
			if (!set1 || !set2){
				return []
			} else {
				
				var intersection = []
				
				for (var i = 0; i < set1.keys.length; i++){
					for (var j = 0; j < set2.keys.length; j++){
						if (set1.keys[i] == set2.keys[j]){
							intersection.push(set1.keys[i])
						}
					}
				}
				
				return intersection
				
			}
			
			
		},
		union: function(selections){			
			var keySets = selections.map(function(item){
				return item.keys;
			});
			return _.union.apply(this,keySets);
		},
		unionByName: function(dimension, names){
			self = this;
			var selections = _.filter(self.selections[dimension], function(item){
				return _.includes(names, item.name);
			});
			return this.selections.union(selections);
		}
	}	
});