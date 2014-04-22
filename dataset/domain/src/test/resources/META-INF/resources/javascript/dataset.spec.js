define ([ 'dataset' ], function (dataset) {
  describe ("dataset", function () {
    it ("should be declared", function () {
      expect (dataset).toBeDefined ();
    });

    it ("should have a decorator", function () {
      expect (dataset.decorate).toBeDefined ();
    });

    describe ("decorator", function () {
      var json = {
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
      };

      dataset.decorate (json);

      describe ("analysis", function () {
        it ("should find existing analysis", function () {
          expect (json.analysis ("mock").name).toBe ("mock");
        });

        it ("should throw error on non-existing analysis", function () {
          expect (function () {
            json.analysis ("notThere").name
          }).toThrow (new Error ("No analysis notThere found for dataset mock"));
        });
      });

      describe ("dimension", function () {
        it ("should find existing dimension", function () {
          expect (json.dimension ("row").keys).toEqual ([ "r1", "r2" ]);
        });

        it ("should throw error on non-existing dimension", function () {
          expect (function () {
            json.dimension ("notThere");
          }).toThrow (new Error ("No dimension notThere found for dataset mock"))
        });
      });
    });
  });
});