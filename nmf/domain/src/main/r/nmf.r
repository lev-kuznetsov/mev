shim ('NMF', callback = function (nmf, basis, coef)
  define (nmf = function () function (dataset, rank = 3, method = "brunet", seed = "nndsvd", nrun = 10) {
    res <- nmf (dataset, rank, method = method, seed = seed, nrun = nrun);
    W <- basis (res);
    H <- coef (res);
    colnames (W) = c (1:dim (W)[ 2 ]);
    rownames (H) = c (1:dim (H)[ 1 ]);
    H_root = hclust (dist (t (H)));
    list (w = W, h = list (matrix = H, root = H_root));
  }), binder = binder ());