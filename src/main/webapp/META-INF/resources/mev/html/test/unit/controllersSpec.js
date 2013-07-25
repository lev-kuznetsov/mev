'use strict';

/* jasmine specs for controllers go here */

describe('myApp controllers', function(){
	
  beforeEach(module('myApp.controllers'));
  
  describe('HeatmapCtrl', function() {
    
    var $rootScope, createController, $httpBackend;
   
    beforeEach(inject(function($injector) {

      $httpBackend = $injector.get('$httpBackend');
      $rootScope = $injector.get('$rootScope');
      var $controller = $injector.get('$controller');


      $httpBackend.whenGET('data/subs/undefined-0.json')
          .respond({
		      data: "test",
		      maxpage: 2,
		      pages: [
					{
					  "index": "All",
					  "class": ""
					},
					{
					  "index": "1",
					  "class": ""
					},
					{
					  "index": "2",
					  "class": "active"
					}]
          });
          
      $httpBackend.whenGET('data/subs/undefined-1.json')
          .respond({
              data: 'test',
              maxpage: 2,
              pages: [
					{
					  "index": "All",
					  "class": ""
					},
					{
					  "index": "1",
					  "class": ""
					},
					{
					  "index": "2",
					  "class": "active"
					}]
           });
  
      
      createController = function() {
        $controller('HeatmapCtrl', {'$scope': $rootScope});
      };

    }));

    afterEach(function() {
		
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
      
    });
    
    it('should not add dupicate rows to the marked rows object', function() {
	   $httpBackend.expectGET('data/subs/undefined-0.json');
       var controller = createController();
       $httpBackend.flush();
       
       $rootScope.storeRow('testrow');
       $rootScope.storeRow('testrow2');
       $rootScope.storeRow('testrow');
       
       expect($rootScope.markedRows.length).toBe(2);
       
	});
	
	it('should clear all marked rows when clearAllRows is called', function() {
		
		$httpBackend.expectGET('data/subs/undefined-0.json');
		var controller = createController();
		$httpBackend.flush();

		$rootScope.storeRow('testrow');
		$rootScope.storeRow('testrow2');
		$rootScope.storeRow('testrow3');
		
		$rootScope.clearAllRows();
		expect($rootScope.markedRows.length).toBe(0);
		
		
	});
	
	it('should not remove a row from the marked rows object if an attempt is made to remove a nonexisting row', function() {
	   
	   $httpBackend.expectGET('data/subs/undefined-0.json');
       var controller = createController();
       $httpBackend.flush();
       
       expect($rootScope.markedRows.length).toBe(0);
       $rootScope.storeRow('testrow');
       $rootScope.storeRow('testrow2');
       $rootScope.removeRow('testrow3');
       
       expect($rootScope.markedRows.length).toBe(2);
	});
    
    it('should fetch data and store ininitial vars when page is loaded', function() {

       $httpBackend.expectGET('data/subs/undefined-0.json');
       var controller = createController();
       $httpBackend.flush();

       expect($rootScope.heatmapdata).toBe('test');
       expect($rootScope.maxpage).toBe(2);
       expect($rootScope.view).toBe('all');
       expect($rootScope.currentpage).toBe(0);

    });
    
    it('should fetch paged data and store into dataset when requestPage is called', function() {

       var controller = createController();
       $httpBackend.flush();
       
       $httpBackend.expectGET('data/subs/undefined-1.json');
       $rootScope.requestPage(1);
       $httpBackend.flush();
       
       expect($rootScope.heatmapdata).toBe('test');
       expect($rootScope.maxpage).toBe(2);
       expect($rootScope.view).toBe('page');
       expect($rootScope.currentpage).toBe(1);
       
    });
    
    it('should update vizColor when updateColor is called', function() {
		
		var controller = createController();
        $httpBackend.flush();
		
        expect($rootScope.vizcolor).toBe("red");
        $rootScope.updateColor("blue");
        expect($rootScope.vizcolor).toBe("blue");
        
    });
    
    it('should not allow vizColor to be an undefined class', function() {
		
		var controller = createController();
        $httpBackend.flush();

        $rootScope.updateColor(20);
        expect($rootScope.vizcolor).toBe("red");
        
    });

    it('should store selected row data into markedRows when storeRow is called', function() {
		
		var controller = createController();
        $httpBackend.flush();
        
        $rootScope.storeRow('testrow');
        expect($rootScope.markedRows).toContain('testrow');
        
    });
    
    it('should remove the row data index from markedRows when removeRow is called with index parameter', function() {
        
        var controller = createController();
        $httpBackend.flush();
        
        $rootScope.storeRow('testrow');
        $rootScope.storeRow('testrow2');
        $rootScope.removeRow('testrow');
        expect($rootScope.markedRows).not.toContain('testrow');
        
    });
   
   
  });

  describe('GeneSelectCtrl', function() {
    
    var $rootScope, createController, $httpBackend;
   
    beforeEach(inject(function($injector) {

      $httpBackend = $injector.get('$httpBackend');
      $rootScope = $injector.get('$rootScope');
      var $controller = $injector.get('$controller');
      
      createController = function() {
		  
          $controller('GeneSelectCtrl', {'$scope': $rootScope});
          
      };

    }));

    afterEach(function() {

      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
   
    });

    
    it('should send a request to download the initial page of data and update vars', function() {
        var controller = createController();
        $httpBackend.flush();
    });

    it('should fail gracefully when a request for page is not available', function() {

    });
    
    it('should update marked genes when genes are marked by user', function () {
      
        var controller = createController();
        $httpBackend.flush();
      
    });

    it('should update marked genes when user asks to remove genes is called', function() {

        var controller = createController();
        $httpBackend.flush();
    });
    
    it('should update genes list when filter is applied', function() {

        var controller = createController();
        $httpBackend.flush();
    });
	
    it('should fail gracefully when filter request is sent and no objects are received', function() {
        var controller = createController();
        $httpBackend.flush();

    });
    
    it('should page up when user requests', function() {
        var controller = createController();
        $httpBackend.flush();
    
    });

    it('should page down when user requests', function() {
        var controller = createController();
        $httpBackend.flush();

    });
    
    it('should not update page down when currently on page 1', function() {
        var controller = createController();
        $httpBackend.flush();
		
    });
    
    it('should not update page up when currently on max page', function() {
        var controller = createController();
        $httpBackend.flush();
		
    });
    
  });
  
});
