"use strict";
var gulp = require('gulp'),
 stealTools = require('steal-tools'),
 path = require('path'),
 _ = require("lodash");

// Load package.json 
var pkg = require('./package.json');
var bundlesPath = pkg.system.bundlesPath || "dist/bundles";
// Steal Build - defaults to Production
var defaults = {
  system: {
    main: pkg.main.replace(".js", ""),
    config: "package.json!npm",
    bundlesPath: bundlesPath + "/min"
  },
  buildOptions: {
    minify: true,
    sourceMaps: false,
    watch: false,
    uglifyOptions: {
      //mangle does not work for angular.js
      //need to provide "reserved" option to UglifyJS
      //but is not currently supported by StealJS 
      //(because it usess the #simpleWay of launching uglify)
      mangle: false      
    },
    bundleSteal: true
  }
};
gulp.task('build', function() {
  stealTools.build(defaults.system, defaults.buildOptions);
});

//correct for dev
gulp.task('build-dev', function() {
  var devConfig = _.merge(_.cloneDeep(defaults), {
    system: {
      bundlesPath: bundlesPath+"/dev"
    },
    buildOptions: {
      minify: false, 
      sourceMaps: false
    }
  });
  console.log("devConfig", devConfig);
  stealTools.build(devConfig.system, devConfig.buildOptions);
});

//augment config with "watch" options
var watchConfig = _.merge({buildOptions: {watch: true}}, defaults);
watchConfig.buildOptions.watch=true;

gulp.task('watch-build', function() {
  stealTools.build(watchConfig.system, watchConfig.buildOptions);
});


//Copy on write to the jetty directory
var source = path.join(__dirname, "..");                  
var destination = source.replace("src/main/javascript", "target/classes");
console.log("source", source);
console.log("destination", destination);
// The default task (called when you run `gulp` from cli)
gulp.task('watch', function(){
  var watcher = gulp.watch(["!"+source+"/**/node_modules/", 
    source+"/*/*.*",
    source+"/*/*/*.*",
    source+"/*/*/*/*.*",
    source+"/*/*/*/*/*.*",
    source+"/*/*/*/*/*/*.*",
    source+"/*/*/*/*/*/*/*.*",
    "!"+source+"/libs/*/node_modules/**",   
    "!"+source+"/ui/node_modules/**"
    ]);
  watcher.on("change", function(event) {
    console.log("File " + event.path + " was " + event.type + ", copying to target...");
    gulp.src(event.path, {base: source}).pipe(gulp.dest(destination));
  });
});

// var jasmineBrowser = require('gulp-jasmine-browser');
// var watch = require('gulp-watch')

// gulp.task('jasmine', function() {
//   // var filesForTest = ["!"+source+"/**/node_modules/", 
//   //   source+"/libs/domain/common/test/TestContext.spec.js",
//   //   source+"/*/Test*.js",
//   //   source+"/*/*/Test*.js",
//   //   source+"/*/*/*/Test*.js",
//   //   source+"/*/*/*/*/Test*.js",
//   //   source+"/*/*/*/*/*/Test*.js",
//   //   source+"/*/*/*/*/*/*/Test*.js",
//   //   "!"+source+"/libs/*/node_modules/**",   
//   //   "!"+source+"/ui/node_modules/**"
//   //   ] 
//   console.log(source+"/libs/domain/common/test/TestContext.spec.js");
//   var filesForTest = [source+"/libs/domain/common/test/TestContext.spec.js"]
//   return gulp.src(filesForTest) 
//     .pipe(watch(filesForTest))
//     .pipe(jasmineBrowser.specRunner())
//     .pipe(jasmineBrowser.server({port: 8888}));
// });

gulp.task("default", ["build"]);