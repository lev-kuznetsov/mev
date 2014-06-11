define ([ 'angular', 'mev', 'mev-services' ], function (ng, mev, services) {
  ng.element (document).ready (function () {
    ng.bootstrap (document, [ 'mev' ]);
  });

  describe ("mev-services", function () {
    it ("module should be injected", function () {
      expect (services).toBeDefined ();
    });

    it ("module should be pushed on the mev object", function () {
      expect (mev.module).toBeDefined ();
    });
  });
});