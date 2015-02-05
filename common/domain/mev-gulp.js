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

        gutil.log("Creating require test.")
        fs.readFile('./target/require.shims', function (err, data) {
            if (!err) {

                gutil.log("No error in reading require shims.")

                var buffer = new Buffer(head + data.toString() + footer)
                mkdirp('./target', {}, function () {
                    gutil.log("Writing test main.")
                    fs.writeFile('./target/require.test.main.js',buffer,function(){
                        gutil.log("Require test writing successful")
                        done()
                    })
                })
            
            }
            
            
        });


        
    }
}

var requireShimsPathsBuilder = function (gulp) {
    return function (done) {
        //load base require
        var rjs = {}
        rjs['baseUrl'] = '/base'
        rjs['paths'] = {}
        rjs['shim'] = {}

        //Add more paths to require rjs for local gbower deps

        bower.commands.list([], {
            paths: true,
            json: true
        })
        .on('end', function (results) {

            var components = recursePkgMain(results)

            //add info for each component
            for (index in components) {

                //add path info
                rjs.paths[components[index].name] = [components[index].main]

                //if shim info is there, add it
                if(typeof components[index].shim != 'undefined'){
                    rjs.shim[components[index].name] = components[index].shim
                }
            }

            //Add bower json shims for main project
            var bowerShims = require('./bower.json').shim
            var bowerShimsKeys = Object.keys(bowerShims)

            for (index in bowerShimsKeys){
                var component = bowerShimsKeys[index]
                rjs.shim[component] = bowerShims[component]
            }


            var body = ""

            //push rjs keys onto body string for output
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
            test: requireTestConfigBuilder(gulp),
            generateShimsPaths: requireShimsPathsBuilder(gulp)
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

        reporters: ['mocha'],

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

    var devDeps = pkgMeta.devDependencies ? Object.keys(pkgMeta.devDependencies) : undefined

    var localMains = []

    //Add packages to local mains for each component in dependencies
    for (index in deps) {

        var component = deps[index]

        var currentPackage = package.dependencies[component]

        var localMain = buildPathsData(currentPackage, component)

        localMains.push(localMain)
    }

    //Add packages to local mains for each component in dev dependencies
    for (index in devDeps) {

        var component = devDeps[index]

        var currentPackage = package.dependencies[component]

        var localMain = buildPathsData(currentPackage, component)

        localMains.push(localMain)

    }

    return localMains

}

function buildPathsData(package, component){

    var pathsData

    if(typeof package.pkgMeta == 'undefined'){
        gutil.log("pkgMeta not found for " + component)
        throw new Error("Error: pkgMeta not found!")
        return
    }

    //If package doesn't have a main property
    if (typeof package.pkgMeta['main'] == 'undefined'){

        //build paths data using package name assuming it's in a js folder
        pathsData = {
            main: component + '/' + 'js/' + package.pkgMeta.name,
            name: component
        }

    } else {

        //If the package main has a prepended ./, clean it
        var cleanMain = (package.pkgMeta.main.indexOf('./') >= 0) ?
            package.pkgMeta.main.split('./')[1] : package.pkgMeta.main

        //Build the paths data using the cleaned main
        pathsData = {
            main: component + '/' + cleanMain.split('.js')[0],
            name: component
        }

    }

    //Add shims data to pathsData if it's defined
    pathsData.shim = package.pkgMeta.shim

    return pathsData

}
