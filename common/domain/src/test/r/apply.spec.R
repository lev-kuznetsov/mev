suite ("Apply facilities", function (c1 = c (1, 2, 3, 4, 5),
                                     c2 = c (3, 4, 5, 6, 7),
                                     c3 = c (5, 6, 7, 8, 9),
                                     c4 = c (2, 3, 4, 5, 6),
                                     c5 = c (4, 5, 6, 7, 8),
                                     c6 = c (6, 7, 8, 9, 0),
                                     row.names = c ('r1', 'r2', 'r3', 'r4', 'r5'),
                                     d = data.frame (c1 = c1, c2 = c2, c3 = c3, c4 = c4, c5 = c5, c6 = c6,
                                                     row.names = row.names)) {
  inject (function (apply) {
    it ("Should be injected", function ()
      expect (is.function (apply)));

    it ("Should iterate over columns", function ()
      expect (all.equal (list (sum (c1), sum (c2), sum (c3), sum (c4), sum (c5), sum (c6)), apply (d, 2, sum))));

    it ("Should iterate over rows", function ()
      expect (all.equal (list (1, 2, 3, 4, 5), apply (d, 1, function (r) r[ ,1 ]))));

    it ("Should preserve row names", function ()
      expect (all.equal (as.list (row.names), apply (d, 1, function (r) rownames (r)[ 1 ]))));

    it ("Should preserve column names", function ()
      expect (all.equal (list ('c1', 'c2', 'c3', 'c4', 'c5', 'c6'), apply (d, 2, function (c) colnames (c)[ 1 ]))));
  });
});