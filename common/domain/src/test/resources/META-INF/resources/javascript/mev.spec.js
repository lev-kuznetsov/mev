define ([ 'mev' ], function (mev) {
  describe ("mev", function () {
    it ("should declare mev object", function () {
      expect (mev).toBeDefined ();
    });

    it ("should have a logger bound", function () {
      expect (mev.log).toBeDefined ();
    });
  });
});
