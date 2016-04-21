 /* global module */
module.exports = function(config) {
    'use strict';

    var SRC_DIR = 'src/main/resources/META-INF/resources/webjars/lisu/webcert/views/';
    var TEST_DIR = 'src/main/resources/META-INF/resources/webjars/lisu/webcert/';
    var BASE_WEBJAR_DIR = '../../target/webjardependencies/';
    var WEBJAR_DIR = 'target/webjardependencies/';
    var NODE_DIR = 'node_modules/';

    config.set({

        // base path, that will be used to resolve files and exclude
        basePath: '../../../../../../../',

        // frameworks to use
        frameworks: [ 'jasmine' ],

        // generate js files from html templates to expose them during testing.
        preprocessors: {
            'src/main/resources/META-INF/resources/webjars/lisu/webcert/views/**/*.html': ['ng-html2js'],
            'src/main/resources/META-INF/resources/webjars/lisu/webcert/**/*.js': ['coverage'],
            'src/main/resources/META-INF/resources/webjars/common/webcert/**/*.scss': ['scss']
        },

        scssPreprocessor: {
            options: {
                sourceMap: true
            }
        },

        ngHtml2JsPreprocessor: {
            // If your build process changes the path to your templates,
            // use stripPrefix and prependPrefix to adjust it.
            stripPrefix: 'src/main/resources/META-INF/resources/',
            prependPrefix: '/web/',

            // the name of the Angular module to create
            moduleName: 'htmlTemplates'
        },

        // list of files / patterns to load in the browser
        files: [

            // Dependencies
               BASE_WEBJAR_DIR + 'jquery/jquery.min.js',
               BASE_WEBJAR_DIR + 'angularjs/angular.js',
               BASE_WEBJAR_DIR + 'angularjs/angular-mocks.js',
               BASE_WEBJAR_DIR + 'angularjs/1.4.10/angular-locale_sv-se.js',
               BASE_WEBJAR_DIR + 'angularjs/angular-cookies.js',
               BASE_WEBJAR_DIR + 'angular-ui-router/angular-ui-router.js',
               BASE_WEBJAR_DIR + 'angularjs/angular-sanitize.js',
               BASE_WEBJAR_DIR + 'angular-ui-bootstrap/ui-bootstrap-tpls.js',
               BASE_WEBJAR_DIR + 'momentjs/moment.js',
               BASE_WEBJAR_DIR + 'common/webcert/module.min.js',
                NODE_DIR + '/karma-read-json/karma-read-json.js',
                NODE_DIR + 'tv4/tv4.js',

            // Load these first
                SRC_DIR + 'messages.js',
                TEST_DIR + 'module.test.js',

            { pattern: SRC_DIR + '**/*' },
            { pattern: TEST_DIR + '**/*.spec.js' }
        ],

        exclude: [ SRC_DIR + 'module.js' ],

        // web server port
        port: 9876,

        // enable / disable colors in the output (reporters and logs)
        colors: true,

        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_WARN,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera (has to be installed with `npm install karma-opera-launcher`)
        // - Safari (only Mac; has to be installed with `npm install karma-safari-launcher`)
        // - PhantomJS
        // - IE (only Windows; has to be installed with `npm install karma-ie-launcher`)
        browsers: [ 'Chrome' ],

        // If browser does not capture in given timeout [ms], kill it
        captureTimeout: 60000,

        // Continuous Integration mode if true, it capture browsers, run tests and exit
        singleRun: false,

        plugins: [
            'karma-jasmine',
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-phantomjs-launcher',
            'karma-mocha-reporter',
            'karma-ng-html2js-preprocessor',
            'karma-scss-preprocessor'
        ],

        reporters: [ 'progress' ]
    });
};
