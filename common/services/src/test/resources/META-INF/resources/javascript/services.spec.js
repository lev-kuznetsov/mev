define ([ 'mev', 'services' ], function (mev, services) {
  describe ("services", function () {
    it ("should have mev object declared", function () {
      expect (mev).toBeDefined ();
    });

    it ("should have services object declared", function () {
      expect (services).toBeDefined ();
    });

    it ("should push the services object onto mev object", function () {
      expect (mev.server).toBeDefined ();
    });

    it ("should have jquery object bound", function () {
      expect (services.jquery).toBeDefined ();
    });
  });
});