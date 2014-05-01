define ([ 'mev', 'dataset.service' ], function (mev, datasetService) {
  describe ("dataset service", function () {
    it ("dataset service should be defined", function () {
      expect (datasetService).toBeDefined ();
    });

    it ("service should be tied to mev object", function () {
      expect (mev.dataset).toBeDefined ();
    });
  });
});