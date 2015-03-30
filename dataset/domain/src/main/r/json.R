shim ('jsonlite', callback = function (fromJSON, toJSON) {
  define (toJson = function () function (x) toJSON (x, auto_unbox = TRUE));
  define (fromJson = function () function (x) fromJSON (x));
  define (result = function (toJson) function (x)
    if (class (x) != 'try-error') {
      r <- toJson (x);
      class (r) <- 'result-json';
      r;
    } else x);
}, binder = binder ());