(function(){

  define([], function(){

    return function(module){

      module.factory('pcaMulti', [function(){

        return function(pcaData, valueAccessor){

          var selections = pcaData.selections.length > 1 ? pcaData.selections.reduce(function(p,c){
        	  return p.elements.concat(c.elements) 
          }) : pcaData.selections[0].elements
          
          var componentData = selections
          .map(function(sample){
              var sampleCs = pcaData.transformedData.map(function(component){
        	  
            	  var transformedComponent = component.loadings.map(function(loading){
            		  
            		  return loading.value * valueAccessor({
	            			  'row': pcaData.dimension == "column" ? loading.name : sample, 
	            			  'column': pcaData.dimension == "column" ? sample : loading.name
	            	      })[0].value
	            	  
            	  }).reduce(function(p,c){ return p + c })
            	  
            	  return {component: component.name, value: transformedComponent}
        		  
        	  })
        	  
        	  var sampleData = pcaData.selections.filter(function(s){ 
        		  
        		  var isIn = false
        		  for (k in s.elements){
        			 if ( s.elements[k] == sample){
        				 isIn = true
        			 }
        		  }
        		  
        		  return isIn
        	  })[0]
              
        	  return {name:sample, components:sampleCs, color: sampleData.color}
          })
          return componentData

        };

      }])        

    }

  })

})()
