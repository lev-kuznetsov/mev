var path = require('path');

module.exports = function(config) {

    console.log(__dirname);
    var source = path.join(__dirname, "..");    

    config.set({
        basePath: '',
        autoWatch: false,

        singleRun: false,
        frameworks: ['jasmine','steal-npm'],

        steal:{
          files:['src/main/**/*.js'],
          testFiles:['src/test/*spec.js']
        },

        reporters: ['progress'],
        

        "browsers":['Chrome'],
        
    });
};
