describe ("deepEquals", function () {
  it ("primitives", function () {
    expect (Object.deepEquals (1, 1)).toBe (true);
    expect (Object.deepEquals (1, 2)).toBe (false);
  });

  it ("lists", function () {
    expect (Object.deepEquals ([ 1, 2 ], [ 1, 2 ])).toBe (true);
    expect (Object.deepEquals ([ 1, 2 ], [ "a", "b" ])).toBe (false);
  });

  it ("objects", function () {
    expect (Object.deepEquals ({
      "foo" : 2,
      "bar" : 3
    }, {
      "bar" : 3,
      "foo" : 2
    }));
  });

  it ("compound", function () {
    expect (Object.deepEquals ({
      "foo" : {
        "x" : 3,
        "y" : 4
      },
      "bar" : {
        "x" : 4,
        "y" : 3
      }
    }, {
      "bar" : {
        "x" : 4,
        "y" : 3
      },
      "foo" : {
        "x" : 3,
        "y" : 4
      }
    })).toBe (true);
  });
});