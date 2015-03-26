(function(){

  define([], function(){

    return function(module){

      module.factory('pcaTransform', [function(){

        return function(pcaData){

          var componentTransforms = {};

          for (var j = 0; j < pcaData.variances.length; j++){
            componentTransforms[pcaData.variances[j].name] = []
          }

          pcaData.data.map(function(preLoad){
            preLoad.loadings.map(function(loading){
              componentTransforms[loading.component].push({
            	  name:preLoad.name, 
            	  value: loading.value
              })
            })
          })

          var componentKeys = Object.keys(componentTransforms)
          
          var components = []
          for (var k=0; k < componentKeys.length; k++){
            components.push({name:componentKeys[k], loadings: componentTransforms[componentKeys[k]]})
          }

          return components

        };

      }])        

    }

  })

})()
