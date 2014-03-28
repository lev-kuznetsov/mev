define(['angular'], function(angular){
    'use strict';
    
    angular.module('Mev.EventManager', ['Mev.events'])
        .config(['Mev.events.eventPipeline', function(eventPipelineProvider){
            eventPipelineProvider.registerRoom('heatmapPageEvents');
            eventPipelineProvider.registerEvent('heatmapPageEvents', 'updateTrees');
            eventPipelineProvider.registerEvent('heatmapPageEvents', 'updateClusters');
            
        }]);
});