define(
        ['jquery', 'angular'],
        function($, angular) {

            return angular
                    .module('myApp.controllers', [])
                    .controller(
                            'HeatmapCtrl',
                            [
                                    '$scope',
                                    '$routeParams',
                                    '$http',
                                    'pseudoRandomStringGenerator',
                                    '$rootScope',
                                    '$location',
                                    function($scope, $routeParams,
                                            $http, prsg, $rS, $loc) {

                                        if (!$routeParams.datasetName) {
                                            $loc.path('/');

                                        };

                                        $scope.pageLoading = true;
                                        $('#loading').modal();

                                        $scope.heatmapId = $routeParams.datasetName;

                                        $scope.colorOptions = [
                                                "Green-Black-Red",
                                                "Yellow-Black-Blue",
                                                "Red-White-Blue"];
                                        
                                        $scope.selectedColor = "Yellow-Black-Blue";

                                        $scope.defaultColors = function() {
                                            $scope.selectedColor = "Yellow-Black-Blue";
                                        }
                                        $scope.defaultColors()

                                        $scope.heatmapData = undefined;
                                        $scope.heatmapLeftTree = undefined;
                                        $scope.heatmapTopTree = undefined;
                                        $scope.heatmapLeftTreeName = undefined;
                                        $scope.heatmapTopTreeName = undefined;
                                        $scope.previousHCLClusters = undefined;
                                        $scope.previousLimmaAnalysis = undefined;
                                        $scope.previousAnalysisList = undefined;

                                        $scope.buildPreviousAnalysisList = function() {

                                            $scope.previousHCLClusters = [];

                                            $scope.previousLimmaAnalysis = [];

                                            $http(
                                                    {
                                                        method : 'GET',
                                                        url : '/dataset/'
                                                                + $routeParams.datasetName
                                                                + '/analysis/',
                                                        params:{format:'json'}
                                                    })
                                                    .then(
                                                            function(
                                                                    prevList) {

                                                                $scope.previousAnalysisList = prevList.data;

                                                                prevList.data
                                                                        .map(function(
                                                                                name) {

                                                                            // $rS.$apply();

                                                                            $http(
                                                                                    {
                                                                                        method : 'GET',
                                                                                        url : '/dataset/'
                                                                                                + $routeParams.datasetName
                                                                                                + '/analysis/'
                                                                                                + name
                                                                                    })
                                                                                    .then(
                                                                                            function(
                                                                                                    d) {

                                                                                                var randstr = prsg(5);
                                                                                                var randstr2 = prsg(5);

                                                                                                if (d.type == "Hierarchical Clustering") {

                                                                                                    $scope.previousHCLClusters
                                                                                                            .push({
                                                                                                                name : name,
                                                                                                                href : "#"
                                                                                                                        + randstr,
                                                                                                                parentId : randstr2,
                                                                                                                dataParent : '#'
                                                                                                                        + randstr2,
                                                                                                                divId : randstr,
                                                                                                                datar : d
                                                                                                            });

                                                                                                } else
                                                                                                    if (d.type == "LIMMA Differential Expression Analysis") {

                                                                                                        $scope.previousLimmaAnalysis
                                                                                                                .push({
                                                                                                                    name : name,
                                                                                                                    href : "#"
                                                                                                                            + randstr,
                                                                                                                    parentId : randstr2,
                                                                                                                    dataParent : '#'
                                                                                                                            + randstr2,
                                                                                                                    divId : randstr,
                                                                                                                    datar : d
                                                                                                                });
                                                                                                    }
                                                                                            });

                                                                        });

                                                            });

                                        };

                                        $scope.updateHeatmapData = function(
                                                prevAnalysis,
                                                textForm) {

                                            $http(
                                                    {
                                                        method : 'POST',
                                                        url : '/dataset/'
                                                                + $routeParams.datasetName
                                                                + '/analysis/'
                                                                + prevAnalysis,
                                                        params : {
                                                            format : 'json'
                                                        }
                                                    })
                                                    .then(
                                                            function() {

                                                                $(
                                                                        '#heatmapTabLink')
                                                                        .trigger(
                                                                                "click");

                                                                $http(
                                                                        {
                                                                            method : 'GET',
                                                                            url : '/dataset/'
                                                                                    + $routeParams.datasetName
                                                                                    + '/data'
                                                                                    + '?format=json',
                                                                            params : {
                                                                                format : json
                                                                            }
                                                                        })
                                                                        .then(
                                                                                function(
                                                                                        data) {

                                                                                    if (data.column.root) {

                                                                                        // apply
                                                                                        // column
                                                                                        // cluster
                                                                                        // to
                                                                                        // dendogram

                                                                                        $scope.heatmapTopTree = data.column.root;

                                                                                    };

                                                                                    if (data.row.root) {

                                                                                        $scope.heatmapLeftTree = data.row.root;

                                                                                    };

                                                                                    // Apply
                                                                                    // new
                                                                                    // ordering
                                                                                    // and
                                                                                    // dataset
                                                                                    // to
                                                                                    // held
                                                                                    // heatmap
                                                                                    $scope.heatmapData = data;

                                                                                },
                                                                                function() {
                                                                                    // Redirect
                                                                                    // to
                                                                                    // home
                                                                                    // if
                                                                                    // errored
                                                                                    // out
                                                                                    $location
                                                                                            .path('/');
                                                                                });

                                                            },
                                                            function() {

                                                                var message = "Could not update heatmap. If "
                                                                        + "problem persists, please contact us."

                                                                var header = "Heatmap Clustering Update Problem (Error Code: "
                                                                        + s
                                                                        + ")"
                                                                alertService
                                                                        .error(
                                                                                message,
                                                                                header);

                                                            })

                                        };

                                    }])
                    .controller(
                            'ImportsCtrl',
                            [
                                    '$scope',
                                    '$http',
                                    function($scope, $http) {

                                        $scope.userUploads = [];

                                        $scope.loadUploads = function() {

                                            $http({
                                                method : 'GET',
                                                url : '/dataset',
                                                params : {
                                                    format : 'json'
                                                }
                                            })
                                                    .then(
                                                            function(
                                                                    response) {
                                                                $scope.userUploads = response.data;
                                                            });
                                        };

                                        $scope.loadUploads();

                                    }])
                    .controller(
                            'MainPanelController',
                            [
                                    '$scope',
                                    '$element',
                                    '$attrs',
                                    function($scope, $element, $attrs) {
                                        
                                        $scope.baseUrl = '/annotations/'
                                                + $scope.heatmapId
                                                + '/annotation';
                                        $scope.annotationsUrl = $scope.baseUrl
                                                + '/column/new/dataset/';

                                        $scope
                                                .$on(
                                                        'ViewAnnotationsEvent',
                                                        function(
                                                                event,
                                                                selection,
                                                                dimension,
                                                                annotationSource) {
                                                            var annotationsUrl = $scope.baseUrl
                                                                    + "/"
                                                                    + dimension
                                                                    + "/";
                                                            if (typeof selection != 'undefined') {
                                                                annotationsUrl += selection.name
                                                                        + "/";
                                                            } else {
                                                                annotationsUrl += "new/";
                                                            }
                                                            if (typeof annotationSource != 'undefined') {
                                                                annotationsUrl += annotationSource
                                                                        + "/";
                                                            } else {
                                                                annotationsUrl += "dataset/";
                                                            }

                                                            if (typeof selection != 'undefined') {
                                                                annotationsUrl += selection.properties.selectionFacetLink;
                                                            }

                                                            $scope.annotationsUrl = annotationsUrl;
                                                            var elm = document
                                                                    .querySelector('#annotationsTabLink');
                                                            $(elm)
                                                                    .trigger(
                                                                            'click');
                                                            // var
                                                            // annotationsTab =
                                                            // angular.element(elm);
                                                            // annotationsTab.trigger("click");
                                                        });

                                    }]);

        });