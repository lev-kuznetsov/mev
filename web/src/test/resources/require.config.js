REQUIREJS_TESTCONFIG = {
  paths : {    
    jasmineJquery : ["jasmine-jquery"],
    ngMocks : [ "//cdnjs.cloudflare.com/ajax/libs/angular.js/1.4.5/angular-mocks" ],    
    qtip : [ "/webjars/qtip2/2.1.1/jquery.qtip" ],
    log4js : [ '/webjars/log4javascript/1.4.5/log4javascript' ]
  },
  map: {
      '*': {
    	  angularMocks: 'ngMocks'	            
      }	        
  },
  shim : {
    "ngMocks" : {
        exports : "ngMocks",
        deps : [ "ng", "jquery" ]
      },
    "jasmineJquery" : {
        deps : ["jquery"]
    }
  },
  waitSeconds : "2"
};