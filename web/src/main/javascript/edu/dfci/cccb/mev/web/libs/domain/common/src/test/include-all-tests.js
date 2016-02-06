var tests = [];
for (var file in window.__karma__.files) {
    if (window.__karma__.files.hasOwnProperty(file)) {
        if (/base\/test\/.*spec\.js$/.test(file)) {
            tests.push(file.substr(6));
        }
    }
}

tests[tests.length] = function() {
    if(window.__karma__) {
        window.__karma__.start();
    }
};

steal.apply(null, tests);