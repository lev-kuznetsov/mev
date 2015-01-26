define ([ './appender', './warn' ], function (log, l) {
  describe ("Injectable warning logger", function () {
    it ("Should log to warning logger", function () {
      spyOn (log, 'warn');
      l ('warn');
      expect (log.warn).toHaveBeenCalled ();
      expect (log.warn).toHaveBeenCalledWith ('warn')
    });
  });
});
