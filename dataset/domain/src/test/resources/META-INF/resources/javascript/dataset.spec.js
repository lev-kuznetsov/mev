define ([ 'dataset', 'underscore' ], function (dataset, _) {
  describe ("dataset", function () {
    it ("should be declared", function () {
      expect (dataset).toBeDefined ();
    });

    var ds = _.extend ({
      "name" : "mock",
      "dimensions" : {
        "row" : {
          "name" : "row",
          "keys" : [ "r1", "r2" ]
        },

        "column" : {
          "name" : "column",
          "keys" : [ "c1", "c2" ]
        }
      },
      "analyses" : {
        "mock" : {
          "name" : "mock"
        }
      }
    }, dataset);

    describe ("analysis", function () {
      it ("should find existing analysis", function () {
        expect (ds.analysis ("mock").name).toBe ("mock");
      });

      it ("should throw error on non-existing analysis", function () {
        expect (function () {
          ds.analysis ("notThere").name
        }).toThrow (new Error ("No analysis notThere found for dataset mock"));
      });
    });

    describe ("dimension", function () {
      it ("should find existing dimension", function () {
        expect (ds.dimension ("row").keys).toEqual ([ "r1", "r2" ]);
      });

      it ("should throw error on non-existing dimension", function () {
        expect (function () {
          ds.dimension ("notThere");
        }).toThrow (new Error ("No dimension notThere found for dataset mock"))
      });
    });
  });
});