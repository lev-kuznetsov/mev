'use strict';

/* jasmine specs for controllers go here */

describe('myApp controllers', function(){
	
  beforeEach(module('myApp.controllers'));

  describe('HeatmapCtrl', function() {
	
    var scope, ctrl, $httpBackend;
	  
    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
      
      //HTTP Mock Setup
      $httpBackend = _$httpBackend_;
      $httpBackend.expectGET('heatmap/test-file').
          respond([
             {data: [1, 2, 3, 4]},
             {rows: 2},
             {columns: 2}
          ]);
      
      
      scope = $rootScope.$new(),
      ctrl = $controller('HeatmapCtrl', {$scope: scope});
            
    }));
    
    describe('data downloading', function() {
		
      beforeEach(function() {
		  
		expect(scope.matrixLocation).toBeUndefined();
		browser().navigateTo('../../app/index.html#/heatmap/test-file');
	  
	  });
	  
      it('should update matrixLocation based on url :matrixLocation parameter', function() {
        expect(binding('matrixLocation')).toBe('test-file');
      });
      
      it('should have loaded matrix data from http request', function() {
        
      });
      
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
  
});
