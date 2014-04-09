describe ("dataset domain", function () {
  var dataset = {
    "name" : "mock",
    "dimensions" : [ {
      "name" : "row",
      "keys" : [ "r1", "r2", "r3" ]
    }, {
      "name" : "column",
      "keys" : [ "c1", "c2", "c3", "c4" ]
    } ],
    "values" : [ {
      "coordinates" : {
        "column" : "c1",
        "row" : "r1"
      },
      "value" : 0.0
    }, {
      "coordinates" : {
        "column" : "c2",
        "row" : "r1"
      },
      "value" : 0.1
    }, {
      "coordinates" : {
        "column" : "c3",
        "row" : "r1"
      },
      "value" : 0.2
    }, {
      "coordinates" : {
        "column" : "c4",
        "row" : "r1"
      },
      "value" : 0.3
    }, {
      "coordinates" : {
        "column" : "c1",
        "row" : "r2"
      },
      "value" : -0.1
    }, {
      "coordinates" : {
        "column" : "c2",
        "row" : "r2"
      },
      "value" : -0.2
    }, {
      "coordinates" : {
        "column" : "c3",
        "row" : "r2"
      },
      "value" : -0.3
    }, {
      "coordinates" : {
        "column" : "c4",
        "row" : "r2"
      },
      "value" : -0.4
    }, {
      "coordinates" : {
        "column" : "c1",
        "row" : "r3"
      },
      "value" : 0.11
    }, {
      "coordinates" : {
        "column" : "c2",
        "row" : "r3"
      },
      "value" : -0.22
    }, {
      "coordinates" : {
        "column" : "c3",
        "row" : "r3"
      },
      "value" : 0.33
    }, {
      "coordinates" : {
        "column" : "c4",
        "row" : "r3"
      },
      "value" : -0.44
    } ],
    "analyses" : []
  };

  describe ("Dataset", function () {
    it ("name", function () {
      expect (new Dataset (dataset).name ()).toBe ("mock");
    });
  });

  describe ("Dimension", function () {
    it ("name", function () {
      expect (new Dataset (dataset).dimensions ()[0].name ()).toBe ("row");
      expect (new Dataset (dataset).dimensions ()[1].name ()).toBe ("column");
    });

    it ("apply", function () {
      expect (new Dataset (dataset).dimensions ()[0].apply (function (key) {
        return key === "r1";
      }, function (value) {
        return value.value;
      })).toEqual ([ .0, .1, .2, .3 ]);
      expect (new Dataset (dataset).dimensions ()[0].apply (function (key) {
        return key === "r2";
      }, function (value) {
        return value.value;
      })).toEqual ([ -.1, -.2, -.3, -.4 ]);
    });
  });

  /*
   * describe ("Values", function () { it ("get", function () { expect (new
   * Dataset (dataset).values ().get ({ "row" : "r2", "column" : "c2" })).toBe
   * (-.2); }); });
   */
});