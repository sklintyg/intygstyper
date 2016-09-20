/* global module, require */
var baseConfig = require('./karma-webcert.conf.js');

var runCoverage = false;
process.argv.forEach(function(a) {
    'use strict';
    if (a.indexOf('--skip-coverage') === 0) {
        var s = a.split('=');
        if (s.length === 2) {
            var value = s[1].trim();
            if (value === 'false') {
                runCoverage = true;
            }
        }
    }
});

module.exports = function(config) {
    'use strict';

    // Load base config
    baseConfig(config);

    // Override base config
    config.set({
        autoWatch: false,
        logLevel: config.LOG_ERROR,
        singleRun: true,

        browsers: [ 'PhantomJS' ],

        plugins: (function() {
            var plugins = [
                'karma-jasmine',
                'karma-junit-reporter',
                'karma-phantomjs-launcher',
                'karma-mocha-reporter',
                'karma-ng-html2js-preprocessor'
            ];
            if (runCoverage) {
                plugins.push('karma-coverage');
            }
            return plugins;
        })(),

        reporters: [ 'dots', 'coverage' ]
    });
};
