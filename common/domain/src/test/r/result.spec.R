suite ('Result transimssion facilities', function () {
  suite ('Json facilities', function ()
    inject (function (toJson) {
      it ('Should have injectable jsonify function', function ()
        expect (is.function (toJson)));

      it ('Should construct json array', function ()
        expect ("[1,\"a\"]" == toJson (list (1, 'a'))));

      it ('Should construct json object', function ()
        expect ("{\"q\":1,\"w\":\"a\"}" == toJson (list (q = 1, w = 'a'))));

      it ('Should construct compound json object', function ()
        expect ("{\"q\":1,\"w\":{\"e\":1,\"r\":\"v\"}}" == toJson (list (q = 1, w = list (e = 1, r = 'v')))));
    }));

  suite ('Result classification', function ()
    inject (function (result) {
      it ('Should have injectable result function', function ()
        expect (is.function (result)));

      it ('Should set class to result-json', function ()
        expect (inherits (result ('foo'), 'result-json')));
    }));
});