suite ("K means clustering API", function () {
  it ("Should define kmeans clustering injectable", function ()
    inject (function (kmeans) expect (is.function (kmeans))));

  suite ("K means clustering", function (data = data.frame (c1 = c (.1, .2, .3, .4),
                                                            c2 = c (.2, .3, .4, .5),
                                                            c3 = c (5, 6, 7, 8),
                                                            row.names = c ('r1', 'r2', 'r3', 'r4')))
    inject (function (kmeans)
      for (k in 1:3)
        it (paste ("Should cluster for", k, "clusters"), function ()
          expect (k == length (kmeans (data, "eu", k))))));
});
