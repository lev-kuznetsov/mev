(function () {
    "use strict";

    var fileRequirements = ['./lib/network_model', './lib/network_service',
        './modules/filter/networkFilter', './modules/preprocess/networkPreprocess',
        './modules/select/networkSelect', './modules/upload/networkUpload',
        './modules/preview/networkPreview', './modules/view/networkView'];

    define(fileRequirements, function (model, service) {

        var moduleDeps = ['networkFilter', 'networkPreprocess', 'networkSelect',
                            'networkUpload', 'networkPreview', 'networkView'];

        angular.module('networkMGMT', moduleDeps).
            service('networkModel', model).
            service('networkService', ['$http', '$q', 'networkModel', service]);

    });

}());