/* global module, require */
var baseConfig = require('./karma-webcert.conf.js');

var runCoverage = process.env.MAVEN_CMD_LINE_ARGS && process.env.MAVEN_CMD_LINE_ARGS.indexOf('-Dskip-coverage=false')  !== -1;

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

        reporters: [ 'dots', 'junit', 'coverage' ],

        coverageReporter: {
            type : 'lcovonly',
            dir : 'target/karma_coverage/webcert',
            subdir: '.'
        },

        junitReporter: {
            outputFile: 'target/surefire-reports/TEST-karma-webcert-test-results.xml'
        }
    });
};
