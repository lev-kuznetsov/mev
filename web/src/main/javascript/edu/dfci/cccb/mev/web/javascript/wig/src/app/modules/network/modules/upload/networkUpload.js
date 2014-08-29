(function () {
    "use strict";

    define(['./lib/upload_model', './lib/upload_functions', './lib/upload_controller'],
        function (model, functions, controller) {

        angular.module('networkUpload', []).
            service('uploadModel', model).
            service('uploadFunctions', ['$http', '$q', functions]).
            controller('uploadController', ['$scope', 'uploadModel', 'uploadFunctions', controller ]).

            directive('ngFileModel', ['$parse', function ($parse) {

                return {
                    restrict: 'A',
                    link: function (scope, e, attrs) {
                        // Bind input value and module
                        e.bind('change', function () {
                            $parse(attrs.ngFileModel).assign(scope, e[0].files);
                            scope.$apply();
                        });
                        // Clear the input after form submit
                        scope.$watch(attrs.ngFileModel, function (file) {
                            if (file === null) {
                                e.val(file);
                            }
                        });

                    }
                };

            }]);

    });

}());