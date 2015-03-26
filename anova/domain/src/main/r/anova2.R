shim ('multtest', callback = function (mt.teststat) {
  # Constructs a correlation list formed in the order of the specified
  # margin of the specified dataframe correlating to its group
  define ('pivot', function () function (df, margin, groups)
    lapply (setNames (dimnames (df)[[ margin ]], dimnames (df)[[ margin ]]), function (name)
      unlist (lapply (if (is.null (names (groups))) 1:length (groups) else names (groups), function (group)
        if (name %in% groups[[ group ]]) group), use.names = FALSE)));

  # Constructs a vector ensuring at most (and optionally at least) single
  # group membership
  define ('relate', function (pivot, relate.allow.unrelated = FALSE)
    function (df, margin, groups, allow.unrelated = relate.allow.unrelated)
      sapply (pivot (df, margin, groups), function (x)
        if (length (x) > 1) stop (paste ("Multiple relationship element"))
        else if (length (x) < 1 && allow.unrelated) NA
        else if (length (x) < 1) stop ("Unrelated element")
        else x));
        
  # Alternative version of 'apply' applied with submatrices of original
  # dataframe preserving row and column names
  define ('apply', function () function (df, margin, fun)
    lapply (1:length (dimnames (df)[[ margin ]]), function (x) fun (switch (margin, `1` = df[x, ], `2` = df[x]))));

  define ('anova', function (apply, relate) function (dataset, groups, p.value, do.adjust) {
    dataset <- dataset[ unlist (groups, use.names = FALSE) ];
    pVals <- 1 - pf (mt.teststat (dataset, relate (dataset, 2, groups), test = "f"), length (groups) - 1, ncol (dataset) - length (groups));
    apply (dataset[ unique (unlist (groups, use.names = FALSE)) ], 1, function (row) {
      list (geneId = rownames (row)[ 1 ], pValue = pVals[ geneId ]);
    })
  });
}, binder = binder ();
            