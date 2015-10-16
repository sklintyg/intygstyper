/* global module, require */
var baseConfig = require('./karma-minaintyg.conf.js');

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

        plugins: [
            'karma-coverage',
            'karma-jasmine',
            'karma-junit-reporter',
            'karma-phantomjs-launcher',
            'karma-mocha-reporter'
        ],

        reporters: [ 'dots', 'junit', 'coverage' ],

        coverageReporter: {
            type : 'lcovonly',
            dir : 'target/karma_coverage/minaintyg',
            subdir: '.'
        },

        junitReporter: {
            outputFile: 'target/surefire-reports/TEST-karma-minaintyg-test-results.xml'
        }
    });
};
