'use strict';

/* jasmine specs for controllers go here */

describe('myApp controllers', function(){
	
  beforeEach(module('myApp.controllers'));

  describe('HeatmapCtrl', function() {
	
  });
  
  describe('AnalyzeCtrl', function() {
	  
  });

  describe('GeneSelectCtrl', function() {
    
    var $rootScope, createController, $httpBackend;
   
    beforeEach(inject(function($injector) {

      $httpBackend = $injector.get('$httpBackend');
      $rootScope = $injector.get('$rootScope');
      var $controller = $injector.get('$controller');
      
      $httpBackend.whenPOST('data/undefined/marks', 'blank').respond(201, '');
      
      $httpBackend.whenGET('data/undefined/marks').respond({test: 'test'});
      $httpBackend.whenGET('data/undefined-2').respond({genes: 'test'});
      $httpBackend.whenGET('data/undefined-1')
                  .respond(
                     {genes:[

                       {
                         name:"test1",
                         symbol:"TEST1",
                         description:"This is the test1",
                         ensembleId: "TEST1ID",
                         pathways: "Test, Test, test"
                       },
                       {
                         name:"test2",
                         symbol:"TEST2",
                         description:"This is the test2",
                         ensembleId: "TEST2ID",
                         pathways: "Test2, Test2, test2"
                       } 

                     ],
                     maxPage: 2,
                     nearbyPages: [
                        {
                          number: 1,
                          link: "testdataset-1"
					    },
					    {
                          number: 2,
                          link: "testdataset-2"
					    },
					  ]
                    });
                  


      createController = function() {
          $controller('GeneSelectCtrl', {'$scope': $rootScope});
      };

    }));

    afterEach(function() {
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();
    });

    
    it('should send a request to download the first page of data and update vars', function() {
       
       $httpBackend.expectGET('data/undefined-1');
       var controller = createController();
       $httpBackend.flush();

       expect($rootScope.genesCurrentPage.length).toBe(2);
       expect($rootScope.pageMax).toBe(2);
       expect($rootScope.currentPage).toBe(1);
       expect($rootScope.nearbyPages.length).toBe(2);
       
    });
    
    it('should update marked genes when genesMarked is called', function () {
      
      var controller = createController();
      $httpBackend.flush();
      
      $httpBackend.expectGET('data/undefined/marks').respond({test: 'test'});
      $rootScope.getMarks();
      $httpBackend.flush();
      
      expect($rootScope.genesMarked.test).toBe('test');
  
    });
	
    it('should update genesSearched when searchGene is called', function() {
        var controller = createController();
        $httpBackend.flush();
        
        $httpBackend.expectGET('data/undefined/q=test').respond({test: 'test'});
        $rootScope.searchGene('test');
        $httpBackend.flush();
        
        expect($rootScope.genesSearched.test).toBe('test');
    });
    
    it('should page up and page down when respective functions are called', function() {
        var controller = createController();
        $httpBackend.flush();
        
        expect($rootScope.currentPage).toBe(1);
        $httpBackend.expectGET('data/undefined-2').respond({genes: 'test'});
        $rootScope.pageUp();
        $httpBackend.flush();
        
        expect($rootScope.currentPage).toBe(2);
        
        $httpBackend.expectGET('data/undefined-1').respond({genes: 'test'});
        $rootScope.pageDown();
        $httpBackend.flush();
        
        expect($rootScope.currentPage).toBe(1);
    });
    
    it('should not update page up when currently on page 1', function() {
        var controller = createController();
        $httpBackend.flush();
        
        expect($rootScope.currentPage).toBe(1);
        
        $rootScope.pageDown();
        
        expect($rootScope.currentPage).toBe(1);
    });
    
    it('should not update page down when currently on max page', function() {
        var controller = createController();
        $httpBackend.flush();
        
        expect($rootScope.currentPage).toBe(1);
        $httpBackend.expectGET('data/undefined-2').respond({genes: 'test'});
        $rootScope.pageUp();
        $httpBackend.flush();
        
        expect($rootScope.currentPage).toBe(2);
        $rootScope.pageUp();
        
        expect($rootScope.currentPage).toBe(2);
    });
    
    it('should update genesMarked when addMark is called', function() {

        var controller = createController();
        $httpBackend.flush();
        

        $httpBackend.expectPOST('data/undefined/marks', 'blank').respond(201, '');
        $rootScope.addMark('blank');
        
        $httpBackend.expectGET('data/undefined/marks').respond({test: 'test'});
        $httpBackend.flush();
        
        expect($rootScope.genesMarked.test).toBe('test');
    });
    
  });
  
});
