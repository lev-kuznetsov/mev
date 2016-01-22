"use strict";
var gulp = require('gulp'),
 stealTools = require('steal-tools'),
 path = require('path'),
 _ = require("lodash");

// Load package.json 
var pkg = require('./package.json')

// Steal Build / Watch the App
var defaults = {
  system: {
    main: pkg.main.replace(".js", ""),
    config: "package.json!npm"
  },
  buildOptions: {
    minify: false,
    sourceMaps: true,
    watch: false,
    uglifyOptions: {
      mangle: false
    },
    bundleSteal: true
  }
};
//augment config with "watch" options
var watchConfig = _.merge({buildOptions: {watch: true}}, defaults);
watchConfig.buildOptions.watch=true;

gulp.task('build', function() {
  stealTools.build(defaults.system, defaults.buildOptions);
});
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
gulp.task("default", ["build"]);