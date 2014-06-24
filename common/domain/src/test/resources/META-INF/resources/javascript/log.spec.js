define ([ 'angular-mocks', 'mev', 'logger', 'log' ], function (ng, mev, log) {
  describe ("angular provided logger module", function () {

    it ("should be in main mev module dependencies", function () {
      expect (mev.requires).toContain ('log');
    });

    beforeEach (module ('mev'));

    it ("should log to log", function () {
      spyOn (log, 'trace');
      inject (function (_$log_) {
        _$log_.log ("ng-log");
      });
      expect (log.trace).toHaveBeenCalled ();
      expect (log.trace).toHaveBeenCalledWith ("ng-log");
    });

    it ("should log to info", function () {
      spyOn (log, 'info');
      inject (function (_$log_) {
        _$log_.info ("ng-info");
      });
      expect (log.info).toHaveBeenCalled ();
      expect (log.info).toHaveBeenCalledWith ("ng-info");
    });

    it ("should log to warn", function () {
      spyOn (log, 'warn');
      inject (function (_$log_) {
        _$log_.warn ("ng-warn");
      });
      expect (log.warn).toHaveBeenCalled ();
      expect (log.warn).toHaveBeenCalledWith ("ng-warn");
    });
    
    it ("should log to error", function () {
      spyOn (log, 'error');
      inject (function (_$log_) {
        _$log_.error ("ng-error");
      });
      expect (log.error).toHaveBeenCalled ();
      expect (log.error).toHaveBeenCalledWith ("ng-error");
    });
    
    it ("should log to debug", function () {
      spyOn (log, 'debug');
      inject (function (_$log_) {
        _$log_.debug ("ng-debug");
      });
      expect (log.debug).toHaveBeenCalled ();
      expect (log.debug).toHaveBeenCalledWith ("ng-debug");
    });
  });
});