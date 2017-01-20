define(['mui',
    './lib/generateView',
    './lib/ProjectClass',
    'dataset/Dataset'], 
        
    function(angular, generateView, ProjectClass){

        return angular.module('Mev.Project', ['Mev.Dataset'])
        .factory('ProjectFactory', ['DatasetFactory', function(DatasetFactory){
            
            //ProjectFactory :: [String], [DatasetResponseObject] -> [Project]
            //  generator function that takes dataset response object and returns
            //  project object.
            return function(datasetName, datasetResponseObj) {
    
                //construction errors
                if(!datasetResponseObj){
                    throw new TypeError("datasetReponseObject not given!") 
                }
                
                if(!datasetName){
                    throw new TypeError("datasetName not given!")
                }
                
                //construct
                var project = new ProjectClass(DatasetFactory(datasetName, datasetResponseObj), generateView);
                project.name=datasetName;
                //properties
                
                return project;
            };
            
        }]);
    
})