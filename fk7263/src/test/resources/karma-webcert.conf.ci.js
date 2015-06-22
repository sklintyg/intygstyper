/* global module, require */
var baseConfig = require('./karma-webcert.conf.js');

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
            'karma-jasmine',
            'karma-junit-reporter',
            'karma-phantomjs-launcher',
            'karma-mocha-reporter',
            'karma-ng-html2js-preprocessor'
        ],

        reporters: [ 'dots', 'junit' ],

        junitReporter: {
            outputFile: 'target/surefire-reports/TEST-karma-webcert-test-results.xml'
        }
    });
};
