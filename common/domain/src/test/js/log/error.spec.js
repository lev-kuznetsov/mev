define ([ './appender', './error' ], function (log, l) {
  describe ("Injectable error logger", function () {
    it ("Should log to error logger", function () {
      spyOn (log, 'error');
      l ('error');
      expect (log.error).toHaveBeenCalled ();
      expect (log.error).toHaveBeenCalledWith ('error')
    });
  });
});
