paths : {
  mev : [ '/classpath/META-INF/resources/javascript/mev' ],
  underscore : [ '/webjars/underscore' ],
  angular : [ '/webjars/angular' ],
  angularResource : [ '/webjars/angular-resource' ],
  angularRoute : [ '/webjars/angular-route' ]
},
shim : {
  'angular' : {
    exports : 'angular'
  },
  'angularRoute' : {
    exports : 'ngRoute',
    deps: [ 'angular' ]
  },
  'angularResource' : {
    exports : 'ngResource',
    deps: [ 'angular' ]
  }
}