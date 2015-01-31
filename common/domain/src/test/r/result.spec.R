suite ('Result transimssion facilities', function () {
  suite ('Json facilities', function ()
    inject (function (toJson, fromJson) {
      suite ('Serialization', function () {
        it ('Should have injectable toJson function', function ()
          expect (is.function (toJson)));

        it ('Should construct json array', function ()
          expect ("[1,\"a\"]" == toJson (list (1, 'a'))));

        it ('Should construct json object', function ()
          expect ("{\"q\":1,\"w\":\"a\"}" == toJson (list (q = 1, w = 'a'))));

        it ('Should construct compound json object', function ()
          expect ("{\"q\":1,\"w\":{\"e\":1,\"r\":\"v\"}}" == toJson (list (q = 1, w = list (e = 1, r = 'v')))));
      });
      suite ('Deserialization', function () {
        it ('Should have injectable fromJson function', function ()
          expect (is.function (fromJson)));

        it ('Should construct unnamed list', function ()
          expect (list (1, 'a') == toJson ("[1,\"a\"]")));

        it ('Should construct named list', function ()
          expect (list (q = 1, w = 'a') == toJson ("{\"q\":1,\"w\":\"a\"}")));
      });
    }));
  suite ('Result classification', function ()
    inject (function (result) {
      it ('Should have injectable result function', function ()
        expect (is.function (result)));

      it ('Should set class to result-json', function ()
        expect (inherits (result ('foo'), 'result-json')));
    }));
});