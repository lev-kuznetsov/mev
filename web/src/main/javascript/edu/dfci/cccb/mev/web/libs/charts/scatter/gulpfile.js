var gulp = require('gulp'),
  stealTools = require('steal-tools');

// Steal Build / Watch the App
gulp.task('steal-build', function() {
  stealTools.build({
    main: 'demo/index',
    config: "package.json!npm"
  },{
    minify: false,
    sourceMaps: true,
    watch: true,
    debug: true
  });
});
gulp.watch(['src/**/*'], ['steal-build']);

// The default task (called when you run `gulp` from cli)
gulp.task('default', [
  'steal-build'  
]);