(function () {

    define([], function () {


        return function (module) {

            module.directive('anovaAccordion', ['tableResultsFilter', 'alertService', 'projectionService', 'pathService',
                function (tableFilter, alertService, projection, paths) {
                    return {
                        restrict: 'E',
                        templateUrl:paths.module + '/templates/anovaAccordion.tpl.html',
                        scope: {
                            analysis: "=analysis",
                            project: '=project',
                        },
                        link: function (scope) {
                        	
                        	//variable to remove embedded pair information
                        	scope.cleanData = undefined;
                        	
                        	scope.$watch('analysis', function (newval) {
                                if (newval) {
                                	scope.cleanData = expandEmbedded(newval.results)
                                    scope.viewGenes()
                                }
                            })
                            
                            scope.$watch('filterParams.pValue.value', function(newval, oldval){
                            	scope.viewGenes()
                            })
                            
                            scope.$watch('filterParams.id.value', function(newval, oldval){
                            	scope.viewGenes()
                            })
                            
                            scope.$watch('filterParams.pairwise_log_fold_change.value', function(newval, oldval){
                            	scope.viewGenes()
                            })
                            
                            scope.$watch('filterParams.pairwise_log_fold_change.op', function(newval, oldval){
                            	scope.viewGenes()
                            })
                            
                            scope.viewGenes = function(){
                        		 scope.filteredResults = tableFilter(scope.cleanData, scope.filterParams);
                        	}

                            scope.headers = [{
                                    'name': 'ID',
                                    'field': "id",
                                    'icon': "search"
                                },{
                                	'name': 'Partner A',
                                	'field': 'partnerA',
                                	'icon': "search"
                                },{
                                	'name': 'Partner B',
                                	'field': 'partnerB',
                                	'icon': "search"
                                },{
                                    'name': 'P-Value',
                                    'field': "pValue",
                                    'icon': "<="
                                },{
                                	'name': 'Pairwise LFC',
                                	'field': 'lfc',
                                	'icon': ["<=", ">="]
                                }
                            ]

                            scope.filterParams = {
                                'id': {
                                    field: 'id',
                                    value: undefined,
                                    op: '~='
                                }, 
                                'pValue': {
                                    field: 'pValue',
                                    value: undefined,
                                    op: '<='
                                },
                                'lfc': {
                                    field: 'lfc',
                                    value: undefined,
                                    op: '<='
                                },
                                'partnerA':{
                                	field:'partnerA',
                                	value:undefined,
                                	op:'~='
                                },
                                'partnerB':{
                                	field:'partnerB',
                                	value:undefined,
                                	op:'~='
                                }
                            };

                            scope.selectionParams = {
                                name: undefined,
                                color: '#' + Math.floor(Math.random() * 0xFFFFFF << 0).toString(16),
                                dimension: 'row'
                            };

                            function traverse(results) {
                                return tableFilter(results, scope.filterParams).map(projection.ids)
                            }

                            scope.addSelections = function () {

                                var keys = traverse(scope.cleanData);
                                var selectionsData = {
                                    name: scope.selectionParams.name,
                                    properties: {
                                        selectionDescription: '',
                                        selectionColor: scope.selectionParams.color,
                                    },
                                    keys: keys
                                };

                                scope.project.dataset.selection.post({
                                        datasetName: scope.project.dataset.datasetName,
                                        dimension: scope.selectionParams.dimension

                                    }, selectionsData,
                                    function (response) {

                                        scope.project.dataset.resetSelections('row')
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

                            };

                            scope.exportParams = {
                                name: undefined,
                                color: '#ffffff'
                            };
                            
                            scope.exportSelection = function () {

                                var keys = traverse(scope.cleanData);
                                var selectionData = {
                                    name: scope.exportParams.name,
                                    properties: {
                                        selectionDescription: '',
                                        selectionColor: scope.exportParams.color,
                                    },
                                    keys: keys
                                };

                                scope.project.dataset.selection.export({
                                        datasetName: scope.project.dataset.datasetName,
                                        dimension: "row"

                                    }, selectionData,
                                    function (response) {
                                        scope.project.dataset.resetSelections('row');
                                        var message = "Added " + scope.exportParams.name + " as new Dataset!";
                                        var header = "New Dataset Export";

                                        alertService.success(message, header);
                                    },
                                    function (data, status, headers, config) {
                                        var message = "Couldn't export new dataset. If " + "problem persists, please contact us.";

                                        var header = "New Dataset Export Problem (Error Code: " + status + ")";

                                        alertService.error(message, header);
                                    });

                            };

                            scope.applyToHeatmap = function () {

                                var labels = traverse(scope.cleanData);

                                scope.project.generateView({
                                    viewType: 'heatmapView',
                                    labels: {
                                        column: {
                                            keys: scope.project.dataset.column.keys
                                        },
                                        row: {
                                            keys: labels
                                        }
                                    },
                                    expression: {
                                        min: scope.project.dataset.expression.min,
                                        max: scope.project.dataset.expression.max,
                                        avg: scope.project.dataset.expression.avg,
                                    }
                                });

                            };
                            
                            function expandEmbedded(data){
                            	//This function is supposed to expand the partner and ratio
                            	// data into their own columns
                            	
                            	var expanded = []
                            	
                            	for (var gene = 0; gene < data.length; gene++){
                            		
                            		for (var pair = 0; pair < data[gene]['pairwise_log_fold_change'].length; pair++){
                            			var cleanRow = {
                            				"id": data[gene]['id'],
                            				"pValue": data[gene]['pValue'],
                            				"lfc": data[gene]['pairwise_log_fold_change'][pair]['ratio'],
                            				'partnerA': data[gene]['pairwise_log_fold_change'][pair]['partnerA'],
                            				'partnerB': data[gene]['pairwise_log_fold_change'][pair]['partnerB']
                            			}
                            			
                            			expanded.push(cleanRow)
                            		}
                            		
                            	}
                            	
                            	return expanded
                            }
                        }
                    };
            }])
            
            return module

        }
    })


})()