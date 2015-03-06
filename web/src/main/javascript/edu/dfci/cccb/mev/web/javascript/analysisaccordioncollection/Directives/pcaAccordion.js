(function(){

    define(['d3'], function(d3){

       return function(module){

           module
           .directive('pcaTest', ['pathService', 'pcaTransform', 'pcaMulti', function(paths, transform, generatePCA){
        	   return {
                   restrict: 'E',
                   templateUrl: paths.module + '/templates/pcaAccordion.test.tpl.html',
                   scope:{
                  	 project: "="
                   },
                   link: function(scope){
                  	 var analysis = {
                  		 "type": "Principal Components", 
                  		 "name":"blah",
                  		 "dimension": "row",
  	                	 'data': [{
  	                		 'name':"GSM74875",
  	                		 'loadings':[
  	                		    {"component":"PC1", value:-3.2955012871945},
  	                		    {"component":"PC2", value:-0.235472001413024},
  	                		    {"component":"PC3", value:1.04249074656539}
  	                		 ]
  	                	 },{
  	                		 'name':"GSM74876", 
  	                		 'loadings':[
  	                		    {"component":"PC1", value:-1.80447105406053},
  	                		    {"component":"PC2", value:0.287475153917424},
  	                		    {"component":"PC3", value:3.68914714714928}
  	                		 ]
  	                	 },{
  	                		 'name':"GSM74877", 
  	                		 'loadings':[
  	                		    {"component":"PC1", value:-3.80096208662079},
  	                		    {"component":"PC2", value:-1.38637595593331},
  	                		    {"component":"PC3", value:0.937030542306353}
  	                		 ]
  	                	 },{
  					          'name':"GSM74878", 
  				              'loadings':[
  				                {"component":"PC1", value:-2.5913389355804},
  	                		    {"component":"PC2", value:1.06757234256587},
  	                		    {"component":"PC3", value:-4.27747174481988}
  					        ]
  	                	 },{
  					          'name':"GSM74879", 
  				              'loadings':[
  				                {"component":"PC1", value:-3.08578471434393},
  	                		    {"component":"PC2", value:0.245581855720878},
  	                		    {"component":"PC3", value:-1.39536472909427}
  					        ]
  	                	 },{
  					          'name':"GSM74880", 
  				              'loadings':[
  				                {"component":"PC1", value:-3.05339947074892},
  	                		    {"component":"PC2", value:0.263186168972665},
  	                		    {"component":"PC3", value:-1.2524157562358}
  					        ]
  	                	 },{
  					          'name':"GSM74881", 
  				              'loadings':[
  				                {"component":"PC1", value:-2.20138689513356},
  	                		    {"component":"PC2", value:0.462750214352787},
  	                		    {"component":"PC3", value:-0.52726916976642}
  					        ]
  	                	 },{
  					          'name':"GSM74882", 
  				              'loadings':[{"component":"PC1","value":-3.47609381386014},{"component":"PC2","value":-0.414456624382498},{"component":"PC3","value":-0.467270143772668}]
  	                	 }],
  	                	 'variances':[{'name':"PC1", 'value':4.28743917531459}, 
  	                	              {'name':"PC2", 'value':3.34996602217847}, 
  	                	              {'name':"PC3", 'value':2.10032793494325}],
  	                	 'randomId': "FTGR",
  	                	 'selections':[{ 
  	                		 name:"S1", 
  	                		 color:"#b4aa84", 
  	                		 elements:[
  	                		     "200016_x_at", 
	            	             "200080_s_at",
	            	             "200050_at",
	            	             "200051_at",
	            	             "200052_s_at",
	            	             "200053_at",
	            	             "200054_at",
	            	             "200055_at"
	            	             ]
  	                	 },{ 
  	                		 name:"S12", 
  	                		 color:"#ef3a84", 
  	                		 elements:[
  	                		     "200698_at",
  	                		     "200700_s_at",
  	                		     "200706_s_at",
  	                		     "200697_at",
	            	             ]
  	                	 }]
  	                	 
                  	 }

                     analysis.transformedData = transform(analysis)
                     analysis.componentSpace = generatePCA(analysis, scope.project.dataset.expression.retrieve)
                     
                     scope.analysis = analysis
                  	 
                   }
          	   }
             }])
           .directive('pcaAccordion', ['pathService', 'alertService', function(paths, alertService){

               return {
                 restrict: 'E',
                 templateUrl: paths.module + '/templates/pcaAccordion.tpl.html',
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
                	 
                	 function transformToPoints(){
                		 
                		 var data = {
                			     'labels': {
                                     x: scope.selectionParams.dimension.x.name,
                                     y: scope.selectionParams.dimension.y.name
                                 },
                                 'points': undefined
                        	 }
                		 
                		 console.log(scope.analysis.componentSpace)
                        	 
                         data.points = scope.analysis.componentSpace
                         .map(function(sample, index){
                        	 
                        	 var x = sample.components.filter(function(comp){ 
                        		 return comp.component == scope.selectionParams.dimension.x.name
                        	 })[0].value
                        	 
                        	 var y = sample.components.filter(function(comp){ 
                        		 return comp.component == scope.selectionParams.dimension.y.name 
                    		 })[0].value
                    		 
                    		 
                             return {
                            	 x:  x, 
                            	 y: y, 
                            	 id: sample.name, 
                            	 fill: sample.color
                             }
                         })
                         
                         return data
                	 };
                 }
              }

           }])

       } 


    })


})()
