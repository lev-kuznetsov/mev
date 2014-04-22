define ([ 'mev' ], function (mev) {
  describe ("mev", function () {
    describe ("deep equals", function () {
      it ("should compare two equivalent json objects and return true", function () {
        expect ({
          "foo" : 1,
          "bar" : 2
        }.equals ({
          "bar" : 2,
          "foo" : 1
        })).toBe (true);
      })

      it ("should compate two different json objects and return false", function () {
        expect ({
          "foo" : 1,
          "bar" : 2
        }.equals ({
          "bar" : 2,
          "foo" : 2
        })).toBe (false);
      })
    });

    it ("should declare mev object", function () {
      expect (mev).toBeDefined ();
    });
  });
});
