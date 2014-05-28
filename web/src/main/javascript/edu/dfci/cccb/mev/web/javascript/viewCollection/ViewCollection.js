define(['angular'], function(angular){
    
    return angular.module('Mev.ViewCollection', [])
    .directive('viewContentItem', ['$compile', function ($compile) {
        var heatmapTemplate = '<vis-Heatmap heatmap-View="view" heatmap-Dataset="project.dataset" project="project"> </vis-Heatmap>';
        var geneBoxPlotTemplate = 'Yo There';
        
        var getTemplate = function(viewType) {
            var template = '';
    
            switch(viewType) {
                case 'heatmapView':
                    template = heatmapTemplate;
                    break;
                case 'geneBoxPlotView':
                    template = geneBoxPlotTemplate;
                    break;
            }
    
            return template;
        }
    
        return {
            restrict: "E",
            rep1ace: true,
            scope: {
                project : '=project',
                view : '=view'
            },
            link: function(scope, element, attrs) {
                

                scope.$watch('view', function(newval){
                    if(newval){

                        element.empty()
                        element.append(getTemplate(scope.view.viewType));
                        $compile(element.contents())(scope);
                    }
                    
                })
                
            }
        };
    }])
    
});