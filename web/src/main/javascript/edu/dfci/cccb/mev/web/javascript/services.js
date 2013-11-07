define ([ 'angular', 'd3' ], function (angular, d3) {

  return angular.module ('myApp.services', []).value ('appVersion', '0.1')
      .value ('appName', 'MEV: Multi-Experiment Viewer').factory (
          'mainMenuBarOptions', [ function () {
            return [ {
              value : "About",
              url : "#"
            }, {
              value : "Contact",
              url : "#"
            } ];
          } ]).factory ('analysisOptions', [ function () {

        // future feature

        return [ {
          name : 'Visualization',
          id : 'VisualLn',
          url : "#blah"

        }, {
          name : 'Clustering',
          id : 'ClustLn',
          url : '',
          sublinks : [ {
            name : "Heirarchical",
            modalid : "#heirarID",
            modalheader : "Heirarchical Clustering",
            modaldir : "modal-Heirarchical"
          } ]
        } ];
      } ]);

});