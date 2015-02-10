var gulp = require('gulp'),
    gutil = require('gulp-util'),
    fs = require('fs'),
    execute = require('child_process').exec,
    async = require('async'),
    mkdirp = require('mkdirp')

gulp.task('testSubs', [], function(done){
    //Tests sub projects
    
    var projects = require('./projects.json')
    
    var iter = function(project, cb){
        
        var cwd = project.directory;

        gutil.log("Beginning test for " + project.project)
        execute('gulp test', {cwd: cwd}, 
        function(err, stdout, stderr) {

            gutil.log(stdout.toString('ascii'))

            if (err) {

                var wrappedError = new Error(stderr.toString('ascii'));

                var error = new gutil
                .PluginError('test', wrappedError, {
                 project: project,
                 message: "Failed test on " + project.project,
                 showStack: false
                })

                cb(error)

            } else {
                cb(null)
            }

        });
        
    }
    
    async.each(projects['modules'], iter, function(err){
        
        if (err){
            gutil.log('Error in async testing.')
            throw err
            return
        }
        
        gutil.log('All sub project tests complete.')
        done()   
    })

})

gulp.task('test',['testSubs'], function (done) {
   return 
});

gulp.task('build', ['prepare'], function(done){
    var projects = require('./projects.json')

    var body = {paths:{}, shims:{}}
    
    var addToShims = function(project, cb){
        gutil.log("Adding shims to for " + project.project)
        fs.readFile(project.directory + '/target/require.shims', function (err, data) {
            if (!err) {
            
                gutil.log("No error in reading require shims for " + project.project)
                var parsed = JSON.parse("{"+ data.toString().slice(0, -2) + "}")
                
                gutil.log("Adding ")
                var shims = parsed.shim? Object.keys(parsed.shim) : []
                var paths = parsed.paths? Object.keys(parsed.paths) : []
                
                for (index in paths){
                    body.paths[paths[index]] = parsed.paths[paths[index]]
                }
                
                for (index in shims){
                    body.shims[shims[index]] = parsed.shim[shims[index]]
                }
                
                var projectMain = require(project.directory + '/bower.json').main 
                
                body.paths[project.project] = [projectMain]
                
                gutil.log("Added shims to for " + project.project)
                cb()
                
                return
            }
            
            cb(err)
            
            
        });
    }
    
    async.each(projects['modules'], addToShims, function(err){
        if (!err){

            var head = 'require.config({ '

            var footer = ",'deps': ['angular', 'app']," +
                "'callback': function(){" +
                "angular.element(document).ready(function() {" +
                "angular.bootstrap(document, ['app']);" +
                "});" +
                "}," +
                "'waitSeconds': 3" +
                "})"
            var bodyString = JSON.stringify(body)
            var buffer = new Buffer(head + bodyString.substring(1,bodyString.length-1) + footer)
            mkdirp('./target', {}, function () {
                fs.writeFile('./target/require.production.main.js',buffer,done)
            })

            return
        }

        var error = new gutil
        .PluginError('build', err, {
         message: "Failed during async generation ",
         showStack: false
        })

        throw error

    })


})

gulp.task('loadSubs', function(done){
   var projects = require('./projects.json')

   var iter = function(project, cb){

      var cwd = project.directory;

      gutil.log("Beginning loading for " + project.project)

      execute('npm install --force', {cwd: cwd},
      function(err, stdout, stderr){
          gutil.log(stdout.toString('ascii'))

          if(err){
            gutil.log("Error in npm install for " + project.project)
            gutil.log(err.message)
          } else {
            gutil.log("Load complete for " + project.project)
          }

          cb(null)
      })
   }

    async.each(projects['modules'], iter, function(err){

       if(err){
         throw err
       }

       done()

    })

})

gulp.task('prepare',['loadSubs'], function(done){

   var projects = require('./projects.json')

   var iter = function(project, cb){

      var cwd = project.directory;

      gutil.log("Beginning prepare loading for " + project.project)

      execute('gulp prepare', {cwd: cwd},
      function(err, stdout, stderr){
          gutil.log(stdout.toString('ascii'))

          if(err){
            gutil.log("Error in gulp prepare for " + project.project)
            gutil.log(err.message)
          } else {
            gutil.log("Prepare loading complete for " + project.project)
          }

          cb(null)
      })
   }

    async.each(projects['modules'], iter, function(err){

       if(err){
         throw err
       }

       done()

    })

})

gulp.task('clean', function(done){
    var projects = require('./projects.json')
    
    var iter = function(project, cb){
        
        var cwd = project.directory;

        gutil.log("Beginning clean for " + project.project)
        execute('gulp clean', {cwd: cwd}, 
        function(err, stdout, stderr) {

            gutil.log(stdout.toString('ascii'))

            var status = null

            if (err) {

                var wrappedError = new Error(stderr.toString('ascii'));

                status = new gutil
                .PluginError('clean', wrappedError, {
                 project: project,
                 message: "Failed bower clean on " + project.project,
                 showStack: false
                })


            } else {
                gutil.log("Bower clean complete for " + project.project)
            }

            cb(status)

        });
        
    }
    
    async.each(projects['modules'], iter, function(err){
        
        if (err){
            gutil.log('Error in async cleaning.')
            throw err
            return
        }
        
        gutil.log('All sub project cleans complete.')
        done()   
    })
})
