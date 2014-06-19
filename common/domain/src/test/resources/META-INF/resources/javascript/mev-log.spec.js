define ([ 'mev-log' ], function (log) {
  describe ("log", function () {
    it ("should be defined", function () {
      expect (log).toBeDefined ();
    });

    it ("should log to error", function () {
      log.error ("error");
    });

    it ("should log to warn", function () {
      log.warn ("warn");
    });

    it ("should log to info", function () {
      log.info ("info");
    });

    it ("should log to debug", function () {
      log.debug ("debug");
    });

    it ("should log to trace", function () {
      log.trace ("trace");
    });
  });
});