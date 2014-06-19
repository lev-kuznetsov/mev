define ([ 'log4js' ], function (log4js) {
  var logger = log4js.getLogger ("mev");

  function AppliedBrowserConsoleAppender () {}

  AppliedBrowserConsoleAppender.prototype = new log4js.BrowserConsoleAppender ();
  AppliedBrowserConsoleAppender.prototype.append = function (event) {
    if (typeof console != "undefined") {
      var message = this.getLayout ().format (event);
      if (!(message instanceof Array)) message = [ messasge ];

      if (console.debug && log4js.Level.DEBUG.isGreaterOrEqual (event.level)) {
        console.debug.apply (console, message);
      } else if (console.info && log4js.Level.INFO.isGreaterOrEqual (event.level)) {
        console.info.apply (console, message);
      } else if (console.warn && log4js.Level.WARN.isGreaterOrEqual (event.level)) {
        console.warn.apply (console, message);
      } else if (console.error && log4js.Level.ERROR.isGreaterOrEqual (event.level)) {
        console.error.apply (console, message);
      } else if (console.log) console.log.apply (console, message);
    }
  };
  log4js.AppliedBrowserConsoleAppender = AppliedBrowserConsoleAppender;

  var appender = new AppliedBrowserConsoleAppender ();
  appender.setLayout (new log4js.NullLayout ());
  logger.addAppender (appender);

  logger.info ("Initialized log");

  return logger;
});