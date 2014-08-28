define ([ 'mev', 'angular-mocks' ], function (mev, ng) {
  describe ("main mev module", function () {
    it ("should be defined", function () {
      expect (mev).toBeDefined ();
    });

    it ("should load angular module", function () {
      module ('mev');
    });
  });

  beforeEach (module ('mev'));

  describe ("services root uri", function () {
    var su = undefined;

    beforeEach (function () {
      inject ([ 'services-root-uri', function (servicesRootUri) {
        su = servicesRootUri;
      } ])
    });

    it ("should be defined", function () {
      expect (su).toBeDefined ();
    });

    it ("should be equal to /services", function () {
      expect (su).toBe ("/services");
    });
  });
});