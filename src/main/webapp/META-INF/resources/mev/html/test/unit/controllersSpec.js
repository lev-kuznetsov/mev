'use strict';

/* jasmine specs for controllers go here */

describe('myApp controllers', function(){
	
  beforeEach(module('myApp.controllers'));

  describe('HeatmapCtrl', function() {
	
    var scope, ctrl, $httpBackend;
	  
    beforeEach(inject(function($rootScope, $controller) {
		
      scope = $rootScope.$new(),
      ctrl = $controller('HeatmapCtrl', {$scope: scope});
            
    }));
    
    it('should have loaded matrix data from http request', function() {
      expect(scope.matrixLocation).toBeUndefined();
    });
    
    it('should generate default cell parameters that match default size', function() {
      //feature pending
    });

    it('should change cell parameters to small from some state', function() {
      //feature pending
    });
	  
    it('should change cell parameters to medium from some state', function() {
      //feature pending
    });

    it('should change cell parameters to large from some state', function() {
      //feature pending
    });
	  
  });
  
  describe('AnalyzeCtrl', function() {
	
    var scope, ctrl, $httpBackend;
	  
    beforeEach(inject(function($rootScope, $controller) {
		
      scope = $rootScope.$new(),
      ctrl = $controller('HeatmapCtrl', {$scope: scope});
            
    }));
    
    it('should load visualization data', function() {
      expect(scope.matrixLocation).toBeUndefined();
    });
    
    it('should load uploads data', function() {
      //feature pending
    });
	  
  });

  describe('GeneSelectCtrl', [function() {
    
    var scope, ctrl, $httpBackend;
   
    beforeEach(inject(function() {

      $httpBackend = _$httpBackend_;
      scope = $rootScope.$new();
      scope.dataset = "testdataset";
      $httpBackend.expectGET('data/testdataset-1')
                  .respond([
                     {genes:[

                       {
                         name:"test1",
                         symbol:"TEST1",
                         description:"This is the test1",
                         ensembleId: "TEST1ID"
                         pathways: "Test, Test, test"
                       },
                       {
                         name:"test2",
                         symbol:"TEST2",
                         description:"This is the test2",
                         ensembleId: "TEST2ID"
                         pathways: "Test2, Test2, test2"
                       } 

                     ]},
                     {maxPage: 2},
                     {nearbyPages: []},
                  ]);

      ctrl = $controller(GeneSelectCtrl, {$scope: scope});
    }));
    
    it('should set the default page to 1', function() {

    });

    it('should have an empty default marked genes list', function() {

    });

    it('should have an empty default searched genes list', function() {

    });

    it('should download the first 20 genes', function() {

    });

  });
  
});
