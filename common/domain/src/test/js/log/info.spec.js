define ([ './appender', './info' ], function (log, l) {
  describe ("Injectable logger to info", function () {
    it ("Should log to info logger", function () {
      spyOn (log, 'info');
      l ('info');
      expect (log.info).toHaveBeenCalled ();
      expect (log.info).toHaveBeenCalledWith ('info')
    });
  });
});
