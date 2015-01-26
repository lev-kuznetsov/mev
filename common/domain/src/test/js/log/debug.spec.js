define ([ './appender', './debug' ], function (log, l) {
  describe ("Injectable logger to debug", function () {
    it ("Should log to debug logger", function () {
      spyOn (log, 'debug');
      l ('debug');
      expect (log.debug).toHaveBeenCalled ();
      expect (log.debug).toHaveBeenCalledWith ('debug')
    });
  });
});
