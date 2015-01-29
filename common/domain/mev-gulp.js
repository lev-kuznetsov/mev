var fs = require('fs'),
    path = require('path'),
    bower = require('bower'),
    gutil = require('gulp-util'),
    mkdirp = require("mkdirp")

var karmaConfigBuilder = function (gulp) {
    //gulp task to build karma file

    return function (done) {

        var head = "module.exports = function(config) {config.set("

        var footer = ")}"

        var body = JSON.stringify(karmaConfig())

        var configString = head + body + footer
        var buffer = new Buffer(configString)
        mkdirp('./target', {}, function () {
            fs.writeFile('./target/karma.conf.js', buffer, done)
        })
    }

}

var requireProdConfigBuilder = function (gulp) {
    return function (done) {

        var head = 'require.config({ '

        var footer = "'deps': ['angular', 'app']," +
            "'callback': function(){" +
            "angular.element(document).ready(function() {" +
            "angular.bootstrap(document, ['app']);" +
            "});" +
            "}," +
            "'waitSeconds': 3" +
            "})"

        //load base require
        var rjs = {}
        rjs.baseUrl = '/base'
        rjs.paths = {}

        //Add more paths to require rjs for local gbower deps

        bower.commands.list([], {
            paths: true,
            json: true
        })
            .on('end', function (results) {

                var locals = recursePkgMain(results)

                for (index in locals) {
                    rjs.paths[locals[index].package] = [locals[index].main]
                }

                for (index in locals) {

                    var shims = locals[index].shim

                    var packageExports = shims ? Object.keys(shims) : []

                    if (packageExports.length > 0) {
                        for (shimIndex in packageExports) {
                            rjs.shim[packageExports[shimIndex]] = locals[index].shim[packageExports[shimIndex]]
                        }
                    }
                }

                var body = ""

                for (keyIndex in Object.keys(rjs)) {
                    var key = Object.keys(rjs)[keyIndex]
                    var dep = "" + key + ": " + JSON.stringify(rjs[key]) + ", "
                    body = body.concat(dep)
                }


                var buffer = new Buffer(head + body + footer)

                mkdirp('./target', {}, function () {
                    fs
                        .writeFile(
                            './target/require.production.main.js',
                            buffer,
                            done
                    )
                })


            })
    }
}

var requireTestConfigBuilder = function (gulp) {
    return function (done) {

        var head = 'var allTestFiles = [];' +
            'var TEST_REGEXP = new RegExp("spec\\\\.js$");var pathToModule = function(path) {' +
            'var baseRep = new RegExp("^\/base\/"); var jsRep = new RegExp("\\\\.js$"); ' +
            'return path.replace(baseRep, "").replace(jsRep, ""); };' +
            'Object.keys(window.__karma__.files).forEach(function(file) { ' +
            '  if (TEST_REGEXP.test(file)) { ' +
            '    allTestFiles.push(pathToModule(file)); ' +
            '  } ' +
            '});require.config({ '

        var footer = "'deps': allTestFiles ,'callback': window.__karma__.start })"

        //Add more paths to require rjs for local gbower deps

        gutil.log("Creating require test.")
        fs.readFile('./target/require.shims', function (err, data) {
            if (!err) {
            
                gutil.log("No error in reading require shims.")
                var buffer = new Buffer(head + data.toString() + footer)
                mkdirp('./target', {}, function () {
                    gutil.log("Writing test main.")
                    fs.writeFile('./target/require.test.main.js',buffer,function(){
                        gutil.log("Test writing successful")
                        done()
                    })
                })
            
            }
            
            
        });


        
    }
}

var requireShimsBuilder = function (gulp) {
    return function (done) {
        //load base require
        var rjs = {}
        rjs['baseUrl'] = '/base'
        rjs['paths'] = {}

        //Add more paths to require rjs for local gbower deps

        bower.commands.list([], {
            paths: true,
            json: true
        })
        .on('end', function (results) {

            var locals = recursePkgMain(results)

            for (index in locals) {
                rjs.paths[locals[index].package] = [locals[index].main]
            }

            for (index in locals) {

                var shims = locals[index].shim

                var packageExports = shims ? Object.keys(shims) : []

                if (packageExports.length > 0) {
                    for (shimIndex in packageExports) {
                        rjs.shim[packageExports[shimIndex]] = locals[index].shim[packageExports[shimIndex]]
                    }
                }
            }

            var body = ""

            for (keyIndex in Object.keys(rjs)) {
                var key = Object.keys(rjs)[keyIndex]
                var dep = '"' + key + '": ' + JSON.stringify(rjs[key]) + ", "
                body = body.concat(dep)
            }


            var buffer = new Buffer(body)

            mkdirp('./target', {}, function () {
                gutil.log("Created target folder")
                fs.writeFile('./target/require.shims',buffer,function(){
                    gutil.log("Shims file generated!")
                    done()
                })
            })
        })
    }
}

module.exports = function (gulp) {
    return {
        karma: karmaConfigBuilder(gulp),
        require: {
            production: requireProdConfigBuilder(gulp),
            test: requireTestConfigBuilder(gulp),
            generateShims: requireShimsBuilder(gulp)
        }
    }
}


//Karma config file
function karmaConfig() {
    return {

        basePath: './js',

        frameworks: ['jasmine', 'requirejs'],

        files: [
            //Test Configuration
            {
                'pattern': '../require.test.main.js',
                'included': true
            },
            //Source Files
            {
                'pattern': '**/*.js',
                'included': false
            },


        ],

        // list of files to exclude
        exclude: [],

        preprocessors: {},

        reporters: ['dots'],

        // web server port
        port: 3000,

        colors: true,

        autoWatch: true,

        browsers: ['PhantomJS'],

        singleRun: false
    }
}

function recursePkgMain(package) {

    var pkgMeta = package.pkgMeta

    var deps = pkgMeta.dependencies ? Object.keys(pkgMeta.dependencies) : undefined

    var localMains = []


    for (index in deps) {

        var key = deps[index]

        if (pkgMeta.dependencies[key].indexOf('../') >= 0) {

            var localMain = {
                main: key + '/' + package.dependencies[key].pkgMeta.main.split('.js')[0],
                package: key,
                shim: package.dependencies[key].pkgMeta.shim ? package.dependencies[key].pkgMeta.shim : undefined
            }
            localMains
                .push(localMain)
        } else {

            var currentMeta = package.dependencies[key].pkgMeta
            
            var localMain = {}
            
            if (typeof currentMeta['main'] != 'undefined'){
                
                var cleanMain = (currentMeta.main.indexOf('./') >= 0) ?
                    currentMeta.main.split('./')[1] : currentMeta.main
                
                localMain = {
                    main: key + '/' + cleanMain.split('.js')[0],
                    package: key,
                    shim: undefined
                }
                
            } else {
                localMain = {
                    main: key + '/' + 'js/' + currentMeta.name + '.js',
                    package: key,
                    shim: undefined
                }
            }
            localMains
                .push(localMain)
        }
    }



    if (localMains.length > 0) {
        var fullMains = localMains

        for (index in localMains) {

            var subPackage = package.dependencies[localMains[index].package]

            fullmains = fullMains
                .concat(recursePkgMain(subPackage))
        }

        return fullMains
    }

    return undefined
}
