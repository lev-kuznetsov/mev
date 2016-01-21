
module.exports = function (grunt) {
  grunt.initConfig({
    "steal-build": {
      default: {
        options: {
          system: {
            main: "stealmain",
            config: "package.json!npm"
          },
          buildOptions: {
            minify: false,
            sourceMaps: true,
            watch: true
          }
        }
      }
    }
  });
  grunt.loadNpmTasks("steal-tools");
  grunt.registerTask("build", ["steal-build"]);
};