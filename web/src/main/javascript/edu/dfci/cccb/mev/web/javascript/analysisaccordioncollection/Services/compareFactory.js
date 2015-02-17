(function(){
	define([],function(){
		return function(module){
			module.factory('compareFactory', [function(){
				return function (defaults){
			    	
			    	//Thing to compare actual to
			    	var bound = defaults ? defaults.bound : undefined
			    	
			    	//property in actual to compare bound to
			    	var key = defaults ? defaults.key : undefined
			    	
			    	//transformer for bound and actual
			    	var transformer = function(input) { return input}
			    	
			    	//comparison operator for actual and bound
			    	// op = '>=' | '<=' | '~=' | '=='
			    	// Note ~= is string contains operation
			    	
			    	var op = defaults ? defaults.op : undefined
			    	
			        var exports = function(actual){
			    		
			    		if (key){
			    			
			    			if (op == '>='){
			    				return (actual[key] && bound) ? 
			        					(transformer(actual[key]) >= transformer(bound)) : true
			    				
			    			} else if (op == '<=') {
			    				return (actual[key] && bound) ? 
			        					(transformer(actual[key]) <= transformer(bound)) : true
			    			} else if (op == '~=') {
			    				// .call used since transformer is String.prototype.indexOf
			    				return (actual[key] && bound) ? 
			        					(transformer.call(actual[key]).indexOf(transformer.call(bound)) > -1  ) : true
			    			}
			    			
			    			return (actual[key] && bound) ? 
			    					(transformer(actual[key]) == transformer(bound)) : true
			    		}
			    		
			    		//If no key was defined
			    		if (op == '>='){
							return (actual && bound) ? 
			    					(transformer(actual) >= transformer(bound)) : true
							
						} else if (op == '<=') {
							return (actual && bound) ? 
			    					(transformer(actual) <= transformer(bound)) : true
						} else if (op == '~=') {
							return (actual && bound) ? 
		        					(transformer(actual).indexOf(transformer(bound)) > -1  ) : true
						}
						
						return (actual && bound) ? 
								(transformer(actual) == transformer(bound)) : true 
			        }
			    	
			    	exports.bound = function(arg){
			    		var self = this
			    		if (arguments.length > 0){
			    			bound = arg
			    			return self
			    		}
			    		return bound
			    	}
			    	
			    	exports.key = function(arg){
			    		var self = this
			    		if (arguments.length > 0){
			    			key = arg
			    			return self
			    		}
			    		return key
			    	}
			    	
			    	exports.op = function(arg){
			    		var self = this
			    		if (arguments.length > 0){
			    			op = arg
			    			return self
			    		}
			    		return op
			    	}
			    	
			    	exports.transformer = function(arg){
			    		
			    		var self = this
			    		if (arguments.length > 0){
			    			transformer = arg
			    			return self
			    		}
			    		return transformer
			    	}
			    	
			        return exports
			    }
			}])
		}
	})
})()