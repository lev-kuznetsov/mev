define ([ 'angular-mocks', 'mev', 'ng-dataset-template-uri' ], function () {
  describe ("dataset template uri", function () {
    var uri = undefined;

    beforeEach (function () {
      module ('mev');
      inject ([ 'dataset-template-uri', function (datasetTemplateUri) {
        uri = datasetTemplateUri;
      } ]);
    });

    it ("should be injected", function () {
      expect (uri).toBeDefined ();
    });
  });
});