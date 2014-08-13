define ([ 'log' ], function (log) {

  if (typeof window.console == "undefined") window.console = {
    error : function () {},
    warn : function () {},
    info : function () {},
    debug : function () {},
    log : function () {}
  };

  describe ("require injected logger", function () {
    it ("should be defined", function () {
      expect (log).toBeDefined ();
    });

    it ("should log to error", function () {
      spyOn (console, 'error');
      log.error ("error");
      expect (console.error).toHaveBeenCalled ();
      expect (console.error).toHaveBeenCalledWith ("error");
    });

    it ("should log to warn", function () {
      spyOn (console, 'warn');
      log.warn ("warn");
      expect (console.warn).toHaveBeenCalled ();
      expect (console.warn).toHaveBeenCalledWith ("warn");
    });

    it ("should log to info", function () {
      spyOn (console, 'info');
      log.info ("info");
      expect (console.info).toHaveBeenCalled ();
      expect (console.info).toHaveBeenCalledWith ("info");
    });

    it ("should log to debug", function () {
      spyOn (console, 'debug');
      log.debug ("debug");
      expect (console.debug).toHaveBeenCalled ();
      expect (console.debug).toHaveBeenCalledWith ("debug");
    });

    it ("should log to trace", function () {
      spyOn (console, 'log');
      log.trace ("trace");
      expect (console.log).toHaveBeenCalled ();
      expect (console.log).toHaveBeenCalledWith ("trace");
    });
  });
});