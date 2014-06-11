define ([ 'angular', 'mev', 'dataset.service' ], function (ng, mev, datasetService) {
  ng.element (document).ready (function () {
    ng.bootstrap (document, [ 'mev' ]);
  });

  describe ("dataset service", function () {
    it ("dataset service should be defined", function () {
      expect (datasetService).toBeDefined ();
    });

    it ("service should be tied to mev object", function () {
      expect (mev.dataset).toBeDefined ();
    });
  });
});