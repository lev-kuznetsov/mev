define(['angular'], function(angular){
    
    return angular.module('Mev.ViewCollection', [])
    .directive('viewContentItem', ['$compile', function ($compile) {
        var heatmapTemplate = '<vis-Heatmap heatmap-View="project.view" heatmap-Dataset="project.dataset" project="project"> </vis-Heatmap>';
        var hiTemplate = 'Yo There';
        
        var getTemplate = function(viewType) {
            var template = '';
    
            switch(viewType) {
                case 'heatmapView':
                    template = heatmapTemplate;
                    break;
                case 'hiThere':
                    template = hiTemplate;
                    break;
            }
    
            return template;
        }
    
        return {
            restrict: "E",
            rep1ace: true,
            scope: {
                project : '=project',
            },
            link: function(scope, element, attrs) {
                

                scope.$watch('project.view', function(newval){
                    if(newval){

                        element.empty()
                        element.append(getTemplate(scope.project.view.viewType));
                        $compile(element.contents())(scope);
                    }
                    
                })
                
            }
        };
    }])
    
});