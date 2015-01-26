define ([ './appender', './log' ], function (log, l) {
  describe ("Injectable logger to trace", function () {
    it ("Should log to trace logger", function () {
      spyOn (log, 'log');
      l ('log');
      expect (log.log).toHaveBeenCalled ();
      expect (log.log).toHaveBeenCalledWith ('log')
    });
  });
});
