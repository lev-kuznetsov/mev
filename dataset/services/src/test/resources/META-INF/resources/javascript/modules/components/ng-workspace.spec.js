define ([ 'angular-mocks', 'mev', 'ng-workspace' ], function () {
  describe ("workspace", function () {
    var workspace = undefined;

    beforeEach (function () {
      module ('mev');
      inject ([ 'workspace', function (ws) {
        workspace = ws;
      } ]);
    });

    it ("should be injected", function () {
      expect (workspace).toBeDefined ();
    });

    describe ("list operation", function () {
      it ("should be bound", function () {
        expect (workspace.list).toBeDefined ();
      });
    });

    describe ("get operation", function () {
      it ("should be bound", function () {
        expect (workspace.get).toBeDefined ();
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