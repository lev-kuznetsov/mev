suite ("Hierarchical clustering API", function () {
  it ("Should define hierarchical clustering injectable", function ()
    inject (function (hclust) expect (is.function (hclust))));

  suite ("Hierachical clustering", function (data = data.frame (c1 = c (.1, .2, .3, .4),
                                                                c2 = c (.2, .3, .4, .5),
                                                                c3 = c (5, 6, 7, 8)))
    inject (function (hclust)
      for (linkage in c ('average', 'centroid', 'complete', 'mcquitty', 'ward'))
        it (paste ("Should cluster using", linkage, "linkage"), function () {
          nodes <- hclust (data,
                           linkage = linkage, distance = 'eu');
          expect (!is.null (nodes$dist));
          expect (!is.null (nodes$left));
          expect (!is.null (nodes$right));
        })));
});