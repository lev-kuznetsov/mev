(function () {
    "use strict";

    define([], function () {

        //Returns the controller. Now this is MVC agnostic and can be shared with other
        //MVC libraries that aren't AngularJS and can truely be reused and tested.
        //There is still some model/controller contamination, since you've linked the definition
        //of networks on the scope through this controller (literally typing the '=' is going
        //down a dark path) but this is still a major step towards having a modular application.
        return function (scope, networkService) {
            scope.networks = networkService;
            scope.networks.fList();

        };

    });

}());