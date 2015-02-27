(function(){

    define(['', function(){

       return function(module){

           module.directive('pcaAccordion', [function(){

               return {
                 restrict: 'E',
                 templateUrl: paths.module + '/templates/pcaAccordion.tpl.html',
                 scope: {
                   analysis: "=analysis",
                   project: "=project"
                 },
                 link: function(scope){
                     
                     scope.dimension = {
                       x: undefined,
                       y: undefined,
                       options: undefined
                     }
                     scope.$watch('analysis', function(analysis, oldval){

                         if (analysis) {
                             //build points using analysis values

                             //update available dimension options

                         }

                     })

                     scope.$watch('dimension.x', function(newval, oldval){
                         if (newval){
                           // update analysis plot data
                         }
                     }) 
                     scope.$watch('dimension.y', function(newval, oldval){
                         if (newval){
                           // update analysis plot data
                         }
                     })

                     scope.on('selectionCreation', function(data){
                         //send data to server
                     })
                 }
              }

           }])

       } 


    })


})()
