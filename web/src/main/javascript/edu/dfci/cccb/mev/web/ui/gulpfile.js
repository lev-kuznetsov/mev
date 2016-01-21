"use strict";
var gulp = require('gulp');
var stealTools = require('steal-tools');

// Steal Build / Watch the App
gulp.task('build', function() {
  stealTools.build({
    main: 'stealmain',
    config: "package.json!npm"
  });
});

var source = '/home/antony/git/mev/web/src/main/javascript/edu/dfci/cccb/mev/web';
var destination = '/home/antony/git/mev/web/target/classes/edu/dfci/cccb/mev/web';
// The default task (called when you run `gulp` from cli)
gulp.task('watch', function(){
  var watcher = gulp.watch(["!"+source+"/**/node_modules/", 
    source+"/ui/*.*",
    source+"/ui/app/**/*.*",
    source+"/ui/less/**/*.*",
    source+"/ui/node_modules/*/*.*",
    source + "/ui/live.js"]);
  watcher.on("change", function(event) {
    console.log("File " + event.path + " was " + event.type + ", copying to target...");
    gulp.src(event.path, {base: source}).pipe(gulp.dest(destination));
  });
});
gulp.task("default", ["watch"]);