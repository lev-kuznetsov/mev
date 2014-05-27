define ([ 'angular', 'angularMocks', 'heatmap/Heatmap', 'jasmineJquery', 'api/Api'], function (angular) {

  describe ("Mev.heatmap", function () {
      
      beforeEach(angular.mock.module('Mev.heatmap'));
      beforeEach(angular.mock.module('Mev.Api'));
      
      describe("HeatmapCtrl", function(){
    	  
          var scope,
          shower = {
			show: function(){
				return null
			},
			hide: function(){
				return null
			}
          },
          $httpBackend;
          
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
              
              //Mock Loading Modal
              
              spyOn(shower, 'show');
              spyOn(shower, 'hide');
              
              _LoadingModal_ = function(){
            	  return shower;
              }
              
                            
              angular.mock.inject(['$rootScope', '$controller', '$injector', 
                                   function($rootScope, $controller, $injector){
                  
            	  $httpBackend = $injector.get('$httpBackend');
            	  //MockDatasetResourceService = $injector.get('DatasetResourceService');
            	  
                  scope = $rootScope.$new();
                  
                  $controller('HeatmapCtrl', {
                      $scope : scope, 
                      $routeParams : _$routeParams_,
                      $location : _$location_,
                      DatasetResourceService : _DatasetResourceService_,//MockDatasetResourceService,
                      ProjectFactory : _ProjectFactory_,
                      'Heatmap.Modal': _LoadingModal_
                  });
                  
              }]);
          
          });
          
          
          // critical
          it('should show the loading modal on page initialization.', function(){
        	  expect(shower.show).toHaveBeenCalled();
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