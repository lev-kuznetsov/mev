"use strict";
define(['./lib/generateView',
    './lib/ProjectClass'],         
    function(generateView, ProjectClass){

        // return angular.module('Mev.Project', arguments, arguments)
        function factory(DatasetFactory){
            
            //ProjectFactory :: [String], [DatasetResponseObject] -> [Project]
            //  generator function that takes dataset response object and returns
            //  project object.
            return function(datasetName, datasetResponseObj) {
    
                //construction errors
                if(!datasetResponseObj){
                    throw new TypeError("datasetReponseObject not given!");
                }
                
                if(!datasetName){
                    throw new TypeError("datasetName not given!");
                }
                
                //construct
                var project = new ProjectClass(DatasetFactory(datasetName, datasetResponseObj), generateView);
                project.name=datasetName;
                //properties
                
                return project;
            };
            
        }
        factory.$inject = ["DatasetFactory"];
        factory.$name = "mevProject";
        factory.$provider = "factory";
        return factory;
    
});