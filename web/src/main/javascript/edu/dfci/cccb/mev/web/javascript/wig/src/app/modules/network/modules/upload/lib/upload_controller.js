(function () {
    "use strict";

    define([], function () {

        return function (scope, model, functions) {
            scope.m = model;
            scope.f = functions;

            scope.f.submit = function () {
                // Disable form
                scope.m.sending = true;

                // Send AJAX POST request
                scope.f.send(scope.m.name, scope.m.network[0], scope.m.note).then(function (data) {
                    // Reset form
                    scope.m.name = '';
                    scope.m.network = null;
                    scope.m.note = '';

                    // Show result for 3 seconds
                    scope.m.msg = data.msg;
                    setTimeout(function () {
                        scope.m.msg = '';
                    }, 3000);

                    // Re-enable form
                    scope.m.sending = false;
                });

            };
        };

    });

}());