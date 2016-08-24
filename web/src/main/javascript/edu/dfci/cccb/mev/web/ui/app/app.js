define(["mui",
	"angular-animate",
	"angular-ui-router",
	"bootstrap",
	"angular-ui-bootstrap",
	"angular-route",
	"app/views/root/views.root.module",
	"app/views/import/views.import.module",
	"app/views/welcome/views.welcome.module",
	"app/widgets/widgets.module",
	"app/utils/utils.module",

	'browser-filesaver',
	'angular-utils-ui-breadcrumbs',

	'ui-router-extras',
	'oclazyload',
	'js/orefine/OrefineBridge',
	'mainmenu'
], function(ng){
	"use strict";
	return ng.module("ngbootstrap-app", ["ct.ui.router.extras"], arguments)
		.config(['$stateProvider', '$urlRouterProvider',
			function($stateProvider, $urlRouterProvider){

				//default page is the welcome page
				$urlRouterProvider.when("/", "/welcome");
				$urlRouterProvider.when("", "/welcome");

				$stateProvider
					.state("root", {
						url: "",
						templateUrl: "app/views/root/templates/root.tpl.html",
						controller: "RootCtrl",
						controllerAs: "RootCtrl",
						abstract: true,
						breadcrumbProxy: "root.welcome"

					})
					.state("root.about", {
						url: "/about",
						templateUrl: "app/views/about/templates/about.tpl.html",
						parent: "root"
					})
					.state("root.contact", {
						url: "/contact",
						templateUrl: "app/views/contact/templates/contact.tpl.html",
						parent: "root",
						data: {
							sidemenuUrl: "app/views/contact/templates/contact.sidemenu.tpl.html"
						}
					})

//		.state("root.issues", {
//			url: "/issues",
//			templateUrl: "app/views/issues/templates/issues.tpl.html",
//			parent: "root",
//			controller: "IssuesVM",
//			data: {
////				sidemenuUrl: "app/views/issues/templates/issues.sidemenu.tpl.html"
//				sidemenuUrl: "app/views/issues/templates/issues.sidemenu.accordion.tpl.html"
//			}
//		})
				;
			}])
		.config(["$futureStateProvider", "$$animateJsProvider", function ($futureStateProvider, $$AnimateJsProvider) {
			$futureStateProvider.stateFactory("lazy", ["$timeout", "$ocLazyLoad", "futureState",
				function($timeout, $ocLazyLoad, futureState){
					return $timeout(function () {
						return System.import(futureState.src).then(function (module) {
							console.log("heavy imported", arguments);
							return $ocLazyLoad.inject(["ng",])
								.then(function(){
									console.log("loaded ng", arguments);
									ng.module("ng").provider("$$animateJs", $$AnimateJsProvider);
									return $ocLazyLoad.load([module])
										.then(function () {
											console.log("heavy loaded", arguments);
										})
										.catch(function (e) {
											throw e;
										});
								});
						}).catch(function (e) {
							throw e;
						})
					});
				}]);
			$futureStateProvider.futureState({
				parent: "root",
				type: "lazy",
				stateName: 'root.datasets',
				src: 'app/views/datasets/views.datasets.module',
				url: "/datasets",
				displayName: "datasets"
			});
			$futureStateProvider.futureState({
				type: "lazy",
				stateName: "root.abstractDataset",
				src: 'app/views/dataset/views.dataset.module',
				parent: "root",
				"abstract": true,
				url: "/dataset",
				breadcrumbProxy: "root.datasets",
				displayName: "datasets",
				template: "<ui-view></ui-view>"
			});
			$futureStateProvider.futureState({
				type: "lazy",
				stateName: 'root.dataset',
				src: 'app/views/dataset/views.dataset.module',
				parent: "root.abstractDataset",
				"abstract": true,
				url: "/:datasetId/"
			});

		}])
		.config(function($provide) {
//	    $provide.decorator('$httpBackend', function($delegate) {
//	        var proxy = function(method, url, data, callback, headers) {
//	            var interceptor = function(status, data, headers) {
//	                var _this = this,
//	                    _arguments = arguments;
//
//	                //most likely returning a template, no delay
//	                if(typeof data==='string' && data.charAt(0)==="<"){
//		                callback.apply(_this, arguments);
//	                }else{
//	                	setTimeout(function() {
//		                    callback.apply(_this, _arguments);
//		                }, 700);
//	                }
//	            };
//	            return $delegate.call(this, method, url, data, interceptor, headers);
//	        };
//	        for(var key in $delegate) {
//	            proxy[key] = $delegate[key];
//	        }
//	        return proxy;
//	    });
		})
		.config(['$sceProvider', function($sceProvider) {
			$sceProvider.enabled(false);
		}])
		// .config(["paginationTemplateProvider", function(paginationTemplateProvider){
		// 	paginationTemplateProvider.setPath('/container/vendor/mbAngularUtils/pagination/dirPagination.tpl.html');
		// }])
		.config(["$httpProvider", function($httpProvider){
			$httpProvider.interceptors.push(function($q, $rootScope) {
				return {
					responseError: function(rejection) {
						console.log("rejection", rejection);
						if(_.includes(rejection.data, "DatasetNotFoundException"))
							$rootScope.$broadcast("mui:error:sessionTimeout", rejection);
						return $q.reject(rejection);
					}
				};
			});
		}])
		.run(["$rootScope", "$state", "$stateParams",
			function ($rootScope, $state, $stateParams) {

//		EndpointConfig();

				$rootScope.$state = $state;
				$rootScope.$stateParams = $stateParams;

				$rootScope.$on('$stateChangeStart',
					function(event, toState, toParams, fromState, fromParams){
//                $("#ui-view").html("");
						$(".page-loading").removeClass("hidden");
					});

				$rootScope.$on('$stateChangeSuccess',
					function(event, toState, toParams, fromState, fromParams){
						$(".page-loading").addClass("hidden");
						if (toState.redirectTo) {
							$state.go(toState.redirectTo, toParams);
						}
					});
			}]);

});