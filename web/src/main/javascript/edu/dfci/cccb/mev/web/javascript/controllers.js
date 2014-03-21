define(
        ['jquery', 'angular', 'notific8'],
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
                                    'logger',
                                    'alertService',
                                    function($scope, $routeParams, $http, prsg, $rS, $loc, log, alertService) {

                                        if (!$routeParams.datasetName) {

                                            $('#loading').modal(
                                                    'hide');
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
                                        };
                                        $scope.defaultColors();

                                        $scope.heatmapData = undefined;
                                        $scope.heatmapLeftTree = undefined;
                                        $scope.heatmapTopTree = undefined;
                                        $scope.heatmapLeftTreeName = undefined;
                                        $scope.heatmapTopTreeName = undefined;
                                        $scope.previousHCLClusters = undefined;
                                        $scope.previousLimmaAnalysis = undefined;
                                        $scope.previousAnalysisList = undefined;
                                        $scope.clickSelectionMode = false;

                                        $scope.buildPreviousAnalysisList = function() {

                                            $scope.previousHCLClusters = [];

                                            $scope.previousLimmaAnalysis = [];

                                            $http(
                                                    {
                                                        method : 'GET',
                                                        url : '/dataset/'
                                                                + $routeParams.datasetName
                                                                + '/data',
                                                        params : {
                                                            format : 'json'
                                                        }
                                                    })
                                                    .success(
                                                            function(data, status, headers, config) {
                                                                $scope.heatmapData = data;
                                                      })
                                                      .error(function(data, status, headers, config) {
                                                          
                                                          var message = "Could not retrieve dataset "
                                                                  + $routeParams.datasetName
                                                                  + ". If the"
                                                                  + "problem persists, please contact us.";
        
                                                          var header = "Heatmap Download Problem (Error Code: "
                                                                  + status
                                                                  + ")";
        
                                                          alertService
                                                                  .error(
                                                                          message,
                                                                          header);
                                                          $loc
                                                                  .path('/');
                                                              });

                                            $http(
                                                    {
                                                        method : 'GET',
                                                        url : '/dataset/'
                                                                + $routeParams.datasetName
                                                                + '/analysis/',
                                                        params : {
                                                            format : 'json'
                                                        }
                                                    })
                                                    .success(
                                                            function(data, status, headers, config) {

                                                                var prevList = data;

                                                                $scope.previousAnalysisList = prevList;

                                                                prevList
                                                                        .map(function(name) {

                                                                            $http(
                                                                                    {
                                                                                        method : 'GET',
                                                                                        url : '/dataset/'
                                                                                                + $routeParams.datasetName
                                                                                                + '/analysis/'
                                                                                                + name,
                                                                                        params : {
                                                                                            format : 'json'
                                                                                        }
                                                                                    })
                                                                                    .success(
                                                                                            function(data, status, headers, config) {

                                                                                                var randstr = prsg(5);
                                                                                                var randstr2 = prsg(5);

                                                                                                if (data.type == "Hierarchical Clustering") {

                                                                                                    $scope.previousHCLClusters
                                                                                                            .push({
                                                                                                                name : name,
                                                                                                                href : "#"
                                                                                                                        + randstr,
                                                                                                                parentId : randstr2,
                                                                                                                dataParent : '#'
                                                                                                                        + randstr2,
                                                                                                                divId : randstr,
                                                                                                                datar : data
                                                                                                            });

                                                                                                } else
                                                                                                    if (data.type == "LIMMA Differential Expression Analysis") {

                                                                                                        $scope.previousLimmaAnalysis
                                                                                                                .push({
                                                                                                                    name : name,
                                                                                                                    href : "#"
                                                                                                                            + randstr,
                                                                                                                    parentId : randstr2,
                                                                                                                    dataParent : '#'
                                                                                                                            + randstr2,
                                                                                                                    divId : randstr,
                                                                                                                    datar : data
                                                                                                                });
                                                                                                    }

                                                                                            })
                                                                                    .error(
                                                                                            function(data, status, headers, config) {
                                                                                                var message = "Could not retrieve previous analysis"
                                                                                                        + name
                                                                                                        + ". If the"
                                                                                                        + "problem persists, please contact us.";

                                                                                                var header = "Heatmap Download Problem (Error Code: "
                                                                                                        + status
                                                                                                        + ")";

                                                                                                alertService
                                                                                                        .error(
                                                                                                                message,
                                                                                                                header);
                                                                                                $loc
                                                                                                        .path('/');
                                                                                            });

                                                                        });

                                                            })
                                                    .error(
                                                            function(data, status, headers, config) {

                                                                var message = "Could not download previous analysis list. If "
                                                                        + "problem persists, please contact us.";

                                                                var header = "Previous Analysis Download Problem (Error Code: "
                                                                        + status
                                                                        + ")";

                                                                alertService
                                                                        .error(
                                                                                message,
                                                                                header);
                                                                $loc
                                                                        .path('/');

                                                            });

                                        };

                                        $scope.updateHeatmapData = function(prevAnalysis, textForm) {

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
                                                    .success(
                                                            function(data, status, headers, config) {
                                                                if (data.type == 'row') {
                                                                    console
                                                                            .log(data)
                                                                    $scope.heatmapLeftTree = data.root;
                                                                    $scope.heatmapData.row = data;
                                                                } else
                                                                    if (res.data.type == 'column') {
                                                                        $scope.heatmapTopTree = data.root;
                                                                        $scope.heatmapData.column = data;
                                                                    }
                                                            })
                                                    .error(
                                                            function(data, status, headers, config) {
                                                                var message = "Could not update heatmap. If "
                                                                        + "problem persists, please contact us.";

                                                                var header = "Heatmap Update Problem (Error Code: "
                                                                        + status
                                                                        + ")";

                                                                alertService
                                                                        .error(
                                                                                message,
                                                                                header);
                                                            });

                                        };

                                    }])
                    .controller(
                            'ImportsCtrl',
                            [
                                    '$scope',
                                    '$http',
                                    'alertService',
                                    function($scope, $http, alertService) {

                                        $scope.userUploads = [];

                                        $scope.loadUploads = function() {

                                            $http({
                                                method : 'GET',
                                                url : '/dataset',
                                                params : {
                                                    format : 'json'
                                                }
                                            })
                                            .success(function(data, status, headers, config) {
                                                                $scope.userUploads = data;
                                                                
                                                                var message = "Could not upload data. If "
                                                                    + "problem persists, please contact us.";

                                                                var header = "Heatmap Update Problem (Error Code: "
                                                                    + status
                                                                    + ")";

                                                                alertService
                                                                    .error(
                                                                            message,
                                                                            header);
                                                                
                                             })
                                             .error(function(data, status, headers, config) {
                                                        var message = "Could not upload data. If "
                                                            + "problem persists, please contact us.";

                                                        var header = "Heatmap Update Problem (Error Code: "
                                                            + status
                                                            + ")";

                                                        alertService
                                                            .error(
                                                                    message,
                                                                    header);
                                              });
                                        };

                                        $scope.loadUploads();

                                    }])
                    .controller(
                            'MainPanelController',
                            [
                                    'MevSelectionService',
                                    '$scope',
                                    '$element',
                                    '$attrs',
                                    function(MevSelectionService, $scope, $element, $attrs) {

                                        $scope.baseUrl = '/annotations/'
                                                + $scope.heatmapId
                                                + '/annotation';
                                        $scope.annotationsUrl = $scope.baseUrl
                                                + '/column/new/dataset/';

                                        $scope.tabs = {};
                                        if ($scope.tabs != undefined) {
                                            $scope.tabs.activeTab = 'visualize';
                                            $scope.tabs.visualizeTabActive = true;
                                            $scope.tabs.annotationsTabActive = false;
                                        }
                                        $scope.setActiveTab = function(name) {
                                            $scope.tabs.activeTab = name;
                                            if (name == 'annotations') {
                                                $scope.tabs.visualizeTabActive = false;
                                                $scope.tabs.annotationsTabActive = true;
                                            } else {
                                                $scope.tabs.visualizeTabActive = true;
                                                $scope.tabs.annotationsTabActive = false;
                                            }

                                        };
                                        $scope.isActiveTab = function(name) {
                                            if ($scope.tabs.activeTab == name)
                                                return true;
                                            else
                                                return false;
                                        };

                                        $scope
                                                .$on(
                                                        'ViewAnnotationsEvent',
                                                        function(event, selection, dimension, annotationSource) {
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
                                                        });

                                        $scope
                                                .$on(
                                                        'SeletionAddedEvent',
                                                        function(dimensionType) {
                                                            console
                                                                    .debug("selection added: "
                                                                            + angular
                                                                                    .toJson(dimensionType)
                                                                            + "$scope.heatmapData.column.selections:"
                                                                            + angular
                                                                                    .toJson($scope.heatmapData.column.selections));
                                                            MevSelectionService
                                                                    .getColumnSelectionQ()
                                                                    .then(
                                                                            function(d) {
                                                                                console
                                                                                        .debug("selections:"
                                                                                                + angular
                                                                                                        .toJson($scope.selections));
                                                                                $scope.heatmapData.column.selections = d;
                                                                            });

                                                        });

                                    }]);

        });