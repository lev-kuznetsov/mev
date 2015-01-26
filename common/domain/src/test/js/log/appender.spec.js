define ([ './appender' ], function (log) {
  describe ('Browser console log4js appender', function () {
    if (typeof window.console == "undefined") window.console = {
      error : function () {},
      warn : function () {},
      info : function () {},
      debug : function () {},
      log : function () {}
    };

    it ("Should log to error", function () {
      spyOn (console, 'error');
      log.error ("error");
      expect (console.error).toHaveBeenCalled ();
      expect (console.error).toHaveBeenCalledWith ("error");
    });

    it ("Should log to warning", function () {
      spyOn (console, 'warn');
      log.warn ("warn");
      expect (console.warn).toHaveBeenCalled ();
      expect (console.warn).toHaveBeenCalledWith ("warn");
    });

    it ("Should log to info", function () {
      spyOn (console, 'info');
      log.info ("info");
      expect (console.info).toHaveBeenCalled ();
      expect (console.info).toHaveBeenCalledWith ("info");
    });

    it ("Should log to debug", function () {
      spyOn (console, 'debug');
      log.debug ("debug");
      expect (console.debug).toHaveBeenCalled ();
      expect (console.debug).toHaveBeenCalledWith ("debug");
    });

    it ("Should log to trace", function () {
      spyOn (console, 'log');
      log.trace ("trace");
      expect (console.log).toHaveBeenCalled ();
      expect (console.log).toHaveBeenCalledWith ("trace");
    });
  });
});
