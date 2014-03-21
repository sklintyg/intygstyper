'use strict';

/* global module */
module.exports = function (config) {
    var WEB_CERT_WEBAPP = "../../../../../inera-webcert/webcert/src/main/webapp/js/";
    config.set({

        autoWatch : true,

        basePath : '../../main/webapp',

        //browsers: ['Chrome', 'ChromeCanary', 'Firefox', 'PhantomJS'],
        browsers : ['PhantomJS'],

        files : [
            WEB_CERT_WEBAPP + 'vendor/angular/1.1.5/angular.js', WEB_CERT_WEBAPP + 'vendor/angular/1.1.5/angular-mocks.js', 'webcert/js/ts-bas-app.js',
            'webcert/js/*.js', '../../test/js/webcert/**/*.js'
        ],

        frameworks : ['jasmine'],

        junitReporter : {
            outputFile : '../../../../target/surefire-reports/karma-test-results.xml'
        },

        plugins : [
            'karma-jasmine', 'karma-junit-reporter', 'karma-chrome-launcher', 'karma-firefox-launcher',
            'karma-phantomjs-launcher'
        ],

        reporters : ['progress', 'junit']
    });
};
