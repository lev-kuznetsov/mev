define ([ 'angular-mocks', 'mev', 'ng-workspace' ], function () {
  describe ("workspace", function () {
    var workspace = undefined;
    var http = undefined;

    beforeEach (function () {
      module ('mev');
      inject ([ 'workspace', '$httpBackend', function (ws, httpBackend) {
        workspace = ws;
        http = httpBackend;
      } ]);
    });

    it ("should be injected", function () {
      expect (workspace).toBeDefined ();
    });

    describe ("list operation", function () {
      it ("should be bound", function () {
        expect (workspace.list).toBeDefined ();
      });

      it ("should list dataset names", function () {
        http.expect ('GET', '/services/dataset').respond ('[ {"name":"first"}, {"name":"second"} ]');

        var list = workspace.list ();
        http.flush ();
        
        expect (list[0].name).toEqual ("first");
        expect (list[1].name).toEqual ("second");
      });
    });

    describe ("get operation", function () {
      it ("should be bound", function () {
        expect (workspace.get).toBeDefined ();
      });
      
      it ("should get dataset", function () {
        http.expect ('GET', '/services/dataset/first').respond ('{"name":"first"}');
        
        var first = workspace.get ("first");
        http.flush ();
        
        expect (first.name).toEqual ("first");
      });
    });

    describe ("download operation", function () {
      it ("should be bound", function () {
        expect (workspace.download).toBeDefined ();
      });
    });

    describe ("upload operation", function () {
      it ("should be bound", function () {
        expect (workspace.upload).toBeDefined ();
      });
    });
  });
});