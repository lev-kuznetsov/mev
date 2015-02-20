suite ("Dataframe grouping facilities", function (c1 = c (1, 2, 3, 4, 5),
                                                  c2 = c (3, 4, 5, 6, 7),
                                                  c3 = c (5, 6, 7, 8, 9),
                                                  c4 = c (2, 3, 4, 5, 6),
                                                  c5 = c (4, 5, 6, 7, 8),
                                                  c6 = c (6, 7, 8, 9, 0),
                                                  row.names = c ('r1', 'r2', 'r3', 'r4', 'r5'),
                                                  d = data.frame (c1 = c1, c2 = c2, c3 = c3, c4 = c4, c5 = c5, c6 = c6,
                                                                  row.names = row.names)) {
  suite ("Pivot facility", function () inject (function (pivot) {
    it ("Should be injected", function () expect (is.function (pivot)));

    it ("Should pivot row groups", function () 
      expect (all.equal (list (r1 = c ('g2', 'g3'), r2 = NULL, r3 = 'g1', r4 = 'g2', r5 = 'g3'),
                         pivot (d, 1, list (g1 = 'r3', g2 = c ('r1', 'r4'), g3 = c ('r1', 'r5'))))));

    it ("Should pivot column groups", function ()
      expect (all.equal (list (c1 = c ('g2', 'g3'), c2 = 'g4', c3 = 'g1', c4 = 'g2', c5 = 'g3', c6 = 'g4'),
                         pivot (d, 2, list (g1 = 'c3', g2 = c ('c1', 'c4'), g3 = c ('c1', 'c5'), g4 = c ('c2', 'c6'))))));
  }));

  suite ("Relate facility", function () inject (function (relate) {
    it ("Should be injected", function () expect (is.function (relate)));

    it ("Should throw error on multiple row membership", function ()
      expect (inherits (try (relate (d, 1, list (g1 = c ('r3', 'r4'), g2 = c ('r1', 'r2', 'r5', 'r3'))), silent = TRUE), 'try-error')));

    it ("Should throw error on multiple column membership", function ()
      expect (inherits (try (relate (d, 2, list (g1 = c ('c3', 'c4', 'c6'), g2 = c ('c1', 'c2', 'c5', 'c3'))), silent = TRUE), 'try-error')));

    it ("Should throw error on unrelated member", function ()
      expect (inherits (try (relate (d, 1, list ('r3', c ('r1', 'r5'))), silent = TRUE), 'try-error')));

    it ("Should not throw error on unrelated member with flag set", function ()
      expect (!inherits (try (relate (d, 1, list ('r3', c ('r1', 'r5')), allow.unrelated = TRUE), silent = TRUE), 'try-error')));

    it ("Should not throw error on unrelated member with default flag set", function () {
      define ('relate.allow.unrelated', function () TRUE);

      inject (function (relate)
        expect (!inherits (try (relate (d, 1, list ('r3', c ('r1', 'r5'))), silent = TRUE), 'try-error')));
    });
  }));
});