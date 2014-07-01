define ([ 'mev', 'angular-mocks' ], function (mev, ng) {
  describe ("main mev module", function () {
    it ("should be defined", function () {
      expect (mev).toBeDefined ();
    });

    it ("should load angular module", function () {
      module ('mev');
    });
  });
});