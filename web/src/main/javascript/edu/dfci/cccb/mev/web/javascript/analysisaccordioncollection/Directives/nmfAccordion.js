(function(){

    define(['d3'], function(d3){

       return function(module){

           module
           .directive('nmfTest', ['pathService', 'pcaTransform', 'pcaMulti', function(paths, transform, generatePCA){
        	   return {
                   restrict: 'E',
                   templateUrl: paths.module + '/templates/nmfAccordion.test.tpl.html',
                   scope:{
                  	 project: "="
                   },
                   link: function(scope){
                  	 
                   }
          	   }
             }])
           .directive('nmfAccordion', ['pathService', 'alertService', function(paths, alertService){

               return {
                 restrict: 'E',
                 templateUrl: paths.module + '/templates/nmfAccordion.tpl.html',
                 scope: {
                   analysis: "=analysis",
                   project: "=project"
                 },
                 link: function(scope){
                	 
                	 scope.visParams = {
                		'width': 700,
                		'height': 520
                	 }
                     
                     scope.selectionParams = {
                         'name':undefined,
                		 'dimension': {
                			 'x': undefined,
                             'y': undefined
                         },
                         'samples':[],
                         'color': '#' + Math
	                         .floor(Math.random() * 0xFFFFFF << 0)
	                         .toString(16)
                     }
                       
                     
                     scope.$watch('analysis', function(analysis, oldval){

                         if (analysis) {
                        	 scope.pc = {data:transformToPoints()}
                         }

                     })

                     scope.$watch('selectionParams.dimension.x', function(newval, oldval){
                         if (newval){
                             scope.pc = {data:transformToPoints()}
                         }
                         return
                     }) 
                     
                     scope.$watch('selectionParams.dimension.y', function(newval, oldval){
                         if (newval){
                             scope.pc = {data:transformToPoints()}
                         }
                         return
                     })
                     
                     scope.addSelections = function(){
                    	 
                         var selectionData = {
                             name: scope.selectionParams.name,
                             properties: {
                                 selectionDescription: '',
                                 selectionColor: scope.selectionParams.color,
                             },
                             keys: scope.selectionParams.samples.map(function(sample){return sample['id']})
                         };

                         scope.project.dataset.selection.post({
                                 datasetName: scope.project.dataset.datasetName,
                                 dimension: "column"

                             }, selectionData,
                             function (response) {
                                 scope.project.dataset.resetSelections('column')
                                 var message = "Added " + scope.selectionParams.name + " as new Selection!";
                                 var header = "Heatmap Selection Addition";
                                 
                                 scope.selectionParams.color = '#' + Math
                                 .floor(Math.random() * 0xFFFFFF << 0)
                                 .toString(16)

                                 alertService.success(message, header);
                             },
                             function (data, status, headers, config) {
                                 var message = "Couldn't add new selection. If " + "problem persists, please contact us.";

                                 var header = "Selection Addition Problem (Error Code: " + status + ")";

                                 alertService.error(message, header);
                             });
                     }
                	 
                 }
              }

           }])

       } 


    })


})()