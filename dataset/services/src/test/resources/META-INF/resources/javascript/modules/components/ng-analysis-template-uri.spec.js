define ([ 'angular-mocks', 'mev', 'ng-analysis-template-uri' ], function () {
  describe ("analysis template uri", function () {
    var uri = undefined;

    beforeEach (function () {
      module ('mev');
      inject ([ 'analysis-template-uri', function (analysisTemplateUri) {
        uri = analysisTemplateUri;
      } ]);
    });

    it ("should be injected", function () {
      expect (uri).toBeDefined ();
    });
  });
});