(function () {
    "use strict";

    define([], function () {

        return function (routeProvider) {
                routeProvider

                    .when('/', {
                        templateUrl: '../static/wig-app/src/app/modules/welcome/welcome.tpl.html'
                    })

                    .when('/settings', {
                        templateUrl: '../static/wig-app/src/app/modules/settings/settings.tpl.html'
                    })

                    .when('/preprocess/:name', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/preprocess/preprocess.tpl.html'
                    })

                    .when('/filter/:name', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/filter/filter.tpl.html'
                    })

                    .when('/filter/:name/ewThr', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/filter/templates/ewThr.tpl.html'
                    })

                    .when('/filter/:name/ndThr', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/filter/templates/ndThr.tpl.html'
                    })

                    .when('/filter/:name/nWL', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/filter/templates/nWL.tpl.html'
                    })

                    .when('/filter/:name/nBL', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/filter/templates/nBL.tpl.html'
                    })

                    .when('/filter/:name/goWL', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/filter/templates/goWL.tpl.html'
                    })

                    .when('/filter/:name/goBL', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/filter/templates/goBL.tpl.html'
                    })

                    .when('/preview/:name', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/preview/preview.tpl.html'
                    })

                    .when('/preview/:name/buildIndex', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/preview/templates/buildIndex.tpl.html'
                    })

                    .when('/preview/:name/chooseCenter', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/preview/templates/chooseCenter.tpl.html'
                    })

                    .when('/select', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/select/select.tpl.html'
                    })

                    .when('/upload', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/upload/upload.tpl.html'
                    })

                    .when('/view/:name', {
                        templateUrl: '../static/wig-app/src/app/modules/network/modules/view/view.tpl.html'
                    })

                    .otherwise({
                        redirectTo: '/'
                    });
            };

    });

}());