define (['angular', 'angularMocks', 'heatmap/Heatmap', 'jasmineJquery', 'api/Api', 'services'], function (angular, am, h, jq,api, services) {

  describe ("Mev.heatmap", function () {

	  //Required Modules
	  beforeEach(angular.mock.module('Mev.Api'));
	  beforeEach(angular.mock.module('Mev.heatmap'));
	  beforeEach(angular.mock.module('myApp.services'));
	  beforeEach(angular.mock.module('ng'));
	  
      describe("HeatmapCtrl", function(){          

    	  var scope = undefined,
		  MockDatasetResourceService = undefined,
		  MockHeatmap = undefined,
		  _$routeParams_ = undefined,
		  _ProjectFactory_ = undefined;
    	  
    	  //Setup
          beforeEach(function(){
 
          	  angular.mock.inject(['$injector', '$controller', '$rootScope', '$httpBackend', '$location', '$resource',
          	                       function($injector, $controller, $rootScope, $httpBackend, $location, $resource){

    			  //$httpBackend = $injector.get('$httpBackend');
    			  
    			  //Injected Deps
    			  expect($injector).toBeDefined();
    			  expect($controller).toBeDefined();
    			  expect($rootScope).toBeDefined();
    			  expect($httpBackend).toBeDefined();
    			  expect($location).toBeDefined();
    			  expect($resource).toBeDefined();
    			  
    			  //Required Test Deps
    			  expect($injector.has('DatasetResourceService')).toBe(true);
    			  
    			  //Mock Deps
    			  scope = $rootScope.$new();
    			  MockDatasetResourceService = $injector.get('DatasetResourceService');
    			  
    			  //Mock Heatmap.modal
    			  MockHeatmap = function(id){
    					return {
    						show: function(){
    							return null
    						},
    						hide: function(){
    							return null
    						}
    					};
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
    			
                  $controller('HeatmapCtrl', {
    				    '$scope' : scope,
    				    '$routeParams' : _$routeParams_,
    				    '$location' : $location,
    				    'DatasetResourceService' : MockDatasetResourceService,//MockDatasetResourceService,
    				    'ProjectFactory' : _ProjectFactory_,
    				    'Heatmap.Modal': MockHeatmap
    			  });
                  
          		  
          	  }])
          });
          
          // critical
          it('should show the loading modal on page initialization.', function(){

          });
          
          xit('should hide the loading modal when a successful dataset call is returned.', function(){
              
          });
      
          xit('should make an http call to fetch the dataset.', function(){

          });
          
          xit('should redirect to the dataset page when there is an error on the http request for a dataset.', function(){
              
          });
          
          xit('should append a project onto the scope when a successful dataset call is returned.', function(){

          });
          
          xit('should tell the project to generate a default view when the project is first loaded.', function(){
              
          });
      
      });
      
  });

});