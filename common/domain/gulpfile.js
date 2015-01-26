var gulp = require('gulp'),
    gutil = require('gulp-util'),
    del = require('del'),
    fs = require('fs'),
    bower = require('bower')

var mev = require('./mev-gulp.js')(gulp)

gulp.task('default', ['test'], function(){
    return
})

gulp.task('loadBower', function(done){ 
    //Loads bower files for other tasks. Necessary for require
    //config build!
    
    var exec = require('child_process').exec;
    
    exec('./node_modules/bower/bin/bower install', {cwd: './'}, 
    function(err, stdout, stderr) {

        if (err) {
          gutil.log(stderr.toString('ascii'))
          throw new Error("Bower Install Error")
        }
        done();
    });
})

gulp.task('targetBower',['loadBower'], function(done){ 
    //Loads bower files in specified local directory

    gulp.src(["bower_components/**/*.js"])
        .pipe(gulp.dest('target/js'))
        .on('end', function(){ 
            done() 
        })
})

gulp.task('karmaConfig', mev.karma)

gulp.task('requireTestConfig', ['targetBower', 'generateTarget', 'generateShimsPaths'], mev.require.test);
gulp.task('requireProdConfig', ['targetBower'], mev.require.production);

gulp.task('prepare',['karmaConfig', 'requireTestConfig'], function(){ 
    return
});

gulp.task('generateTarget', function(done){
    
    gulp.src(["src/test/js/**/*.js", "src/main/js/**/*.js"])
        .pipe(gulp.dest('target/js'))
        .on('end', function(){ 
            done() 
        })
    
})

gulp.task('test',['prepare'], function (done) {
    //Runs karma tests with single run
    

    var karma = require('karma').server;
    
    karma.start({
        configFile: __dirname + '/target/karma.conf.js',
        singleRun: true
    }, done);
    
});

gulp.task('bdd',['prepare'], function (done) {
    //Runs karma tests as bdd
    
    var karma = require('karma').server;
    
    karma.start({
        configFile: __dirname + '/src/test/js/karma.conf.js',
        singleRun: false
    }, done);
    
});

gulp.task('clean', function (cb) {
    //cleans all generated and fetched files for rebuilding
    
    del([
      'target/**',
      'bower_components/'
    ], cb);
});

gulp.task('generateShimsPaths',['loadBower'], mev.require.generateShims)