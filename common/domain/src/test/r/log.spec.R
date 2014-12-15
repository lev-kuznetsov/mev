suite ('Logger injectables', function () {
  it ('Should have injectable debug log',
      function ()
        inject (function (debug) expect (is.function (debug))));

  it ('Should have injectable info log',
      function ()
        inject (function (info) expect (is.function (info))));

  it ('Should have injectable warn log',
      function ()
        inject (function (warn) expect (is.function (warn))));

  it ('Should have injectable error log',
      function ()
        inject (function (error) expect (is.function (error))));
});
