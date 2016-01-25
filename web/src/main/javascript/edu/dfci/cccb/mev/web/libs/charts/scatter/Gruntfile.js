module.exports = function (grunt) {
  grunt.initConfig({
    "steal-build": {
      default: {
        options: {
          system: {
            main: "demo/index",
            config: "package.json!npm"
          },
          buildOptions: {
            minify: false,
            sourceMaps: true,
            watch: true
          }
        }
      }
    },
    "steal-live-reload": {
    default: {
      options: {
        system: {
            main: "demo/index",
            config: "package.json!npm"
          },
          buildOptions: {
            minify: false,
            sourceMaps: true,
            watch: true
          },
          liveReloadOptions: {
            liveReloadPort: 8013
          }
        }
      }
    }
  });

  grunt.loadNpmTasks("steal-tools");
  grunt.registerTask("build", ["steal-build"]);
  grunt.registerTask("live", ["steal-live-reload"]);
};
