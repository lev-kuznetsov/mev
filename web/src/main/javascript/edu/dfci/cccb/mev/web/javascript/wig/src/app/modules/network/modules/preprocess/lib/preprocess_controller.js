(function () {
    "use strict";

    define([], function () {

        return function (scope, model, functions, interval) {
            scope.m = model;
            scope.f = functions;

            // First check and interval

            scope.f.check(scope.m.name).then(function (data) {
                console.log(data);
                scope.m.duration = data.duration;
                scope.m.status = data.status;
                scope.m.status_label = data.status_label.split("\\n");
                scope.m.percentage = data.percentage;
            });

            interval(function () {
            	if (100 != scope.m.percentage) {
	                scope.f.check(scope.m.name).then(function (data) {
		                console.log(data);
		                scope.m.duration = data.duration;
		                scope.m.status = data.status;
		                scope.m.status_label = data.status_label.split("\\n");
		                scope.m.percentage = data.percentage;
		            });
	            }
            }, 5000);

            // Preprocess?

            setTimeout(function () {

                if ('0' === scope.m.status) {
                    console.log('starting');
                    scope.f.start(scope.m.name);

                } else {
                    console.log('not started');
                }

            }, 5000);

        };

    });

}());