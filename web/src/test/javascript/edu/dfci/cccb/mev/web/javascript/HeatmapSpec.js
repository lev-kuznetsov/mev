define ([ 'angular', 'angularMocks', 'heatmap/Heatmap', 'jasmineJquery'], function (angular) {

  describe ("Mev.heatmap", function () {
      
      beforeEach(angular.mock.module('Mev.heatmap'));
      
      describe("HeatmapCtrl", function(){
          var scope;
          
          beforeEach(function(){
             
              //Mock DatasetResourceService
              _DatasetResourceService_ = {
                      get: function(params, success, failure) {
                          _response = {
                                  $promise:{},
                                  $resolved: true,
                                  avg: 24.8148,
                                  min: -21.6599,
                                  max: 75.22613,
                                  row:{
                                      keys:[]
                                  },
                                  column : {
                                      keys:[]
                                  },
                                  values : []
                          }
                          return _response
                      }
              }
              
              //Mock Location setup
              _$location_ = {
                  pathValue: '/dataset',
                  path: function(setter){
                      if(setter){
                          this.pathValue = setter;
                      } else {
                          return this.pathValue;
                      }
                          
                  }
              };
              
              //Mock RouteParams setup
              _$routeParams_ = {
                  datasetNameValue:'tester',
                  datasetName: function(setter){
                      if (setter){
                          this.datasetNameValue = setter;
                      } else {
                          return this.datasetNameValue;
                      }
                      
                  }
              };
              
              //Mock ProjectFactory setup
              _ProjectFactory_ = function(name, dataset){
                  return {
                      dataset : dataset,
                      generateView :function(){
                          this.view = {}
                      },
                      view : undefined
                  }
              };
                            
              angular.mock.inject(['$rootScope', '$controller', function($rootScope, $controller){
                  
                  scope = $rootScope.$new();
                  
                  $controller('HeatmapCtrl', {
                      $scope : scope, 
                      $routeParams : _$routeParams_,
                      $location : _$location_,
                      DatasetResourceService : _DatasetResourceService_,
                      ProjectFactory : _ProjectFactory_
                  });
              }])
          
          
          });
          
          
          // critical
          it('should show the loading modal on page initialization.', function(){
              
          });
          
          it('should hide the loading modal when a successful dataset call is returned.', function(){
              
          });
      
          it('should make an http call to fetch the dataset.', function(){

          });
          
          it('should redirect to the dataset page when there is an error on the http request for a dataset.', function(){
              
          });
          
          it('should append a project onto the scope when a successful dataset call is returned.', function(){

          });
          
          it('should tell the project to generate a default view when the project is first loaded.', function(){
              
          });
      
      });
      
  });

});