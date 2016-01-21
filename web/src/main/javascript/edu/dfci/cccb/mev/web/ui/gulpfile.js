"use strict";
var gulp = require('gulp'),
 stealTools = require('steal-tools'),
 path = require('path');

// Steal Build / Watch the App
gulp.task('build', function() {
  stealTools.build({
    main: 'stealmain',
    config: "package.json!npm"
  });
});

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
gulp.task("default", ["watch"]);