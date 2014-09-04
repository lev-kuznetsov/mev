define ([ 'mev', 'ng-dataset', 'angular-mocks' ], function (mev) {
  describe ("dataset service module", function () {
    it ("should attach itself as dependency to mev", function () {
      expect (mev.requires).toContain ('dataset');
    });
  });
});