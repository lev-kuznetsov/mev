define ([ 'mev', 'angular-mocks' ], function (mev) {
  console.log (mev);

  describe ("MeV core module", function () {

    beforeEach (module ('mev'));

    describe ("MeV logger", function () {
      var $log = undefined;

      beforeEach (function () {
        inject ([ '$log', function (log) {
          $log = log;
        } ]);
      });
      
      it ("Should have a logger", function () {
        expect ($log).toBeDefined ();
      });
    });
  });
});
