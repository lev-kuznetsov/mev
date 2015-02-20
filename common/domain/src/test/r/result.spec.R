suite ('Result transimssion facilities', function () {
  suite ('Json facilities', function ()
    inject (function (toJson, fromJson) {
      it ('Should have injectable toJson function', function ()
        expect (is.function (toJson)));
  
      it ('Should have injectable fromJson function', function ()
        expect (is.function (fromJson)));
    }));
  suite ('Result classification', function ()
    inject (function (result) {
      it ('Should have injectable result function', function ()
        expect (is.function (result)));

      it ('Should set class to result-json', function ()
        expect (inherits (result ('foo'), 'result-json')));
    }));
});