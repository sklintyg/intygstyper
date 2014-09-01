/* global module */
module.exports = function(grunt) {
    'use strict';

    grunt.loadNpmTasks('grunt-contrib-csslint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-ng-annotate');

    var SRC_DIR = 'src/main/resources/META-INF/resources/';
    var TEST_DIR = 'src/test/js/';
    var DEST_DIR = 'target/classes/META-INF/resources/';

    var minaintyg = grunt.file.readJSON(SRC_DIR + 'webjars/common/minaintyg/js/module-deps.json').map(function(file) {
        return file.replace(/\/web\//g, SRC_DIR);
    });
    minaintyg = [SRC_DIR + 'webjars/common/minaintyg/js/module.js'].concat(minaintyg);

    var webcert = grunt.file.readJSON(SRC_DIR + 'webjars/common/webcert/js/module-deps.json').map(function(file) {
        return file.replace(/\/web\//g, SRC_DIR);
    });
    webcert = [SRC_DIR + 'webjars/common/webcert/js/module.js'].concat(webcert);

    grunt.initConfig({

        csslint: {
            options: {
                csslintrc: '../src/main/resources/.csslintrc',
                force: true
            },
            minaintyg: {
                src: [ SRC_DIR + 'webjars/common/minaintyg/**/*.css' ]
            },
            webcert: {
                src: [ SRC_DIR + 'webjars/common/webcert/**/*.css' ]
            }
        },

        concat: {
            minaintyg: {
                src: minaintyg,
                dest: DEST_DIR + 'webjars/common/minaintyg/js/module.min.js'
            },
            webcert: {
                src: webcert,
                dest: DEST_DIR + 'webjars/common/webcert/js/module.min.js'
            }
        },

        jshint: {
            options: {
                jshintrc: '../src/main/resources/.jshintrc',
                force: true
            },
            minaintyg: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/common/minaintyg/**/*.js', TEST_DIR + 'minaintyg/**/*.js' ]
            },
            webcert: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/common/webcert/**/*.js', TEST_DIR + 'webcert/**/*.js' ]
            }
        },

        karma: {
            minaintyg: {
                configFile: 'src/test/resources/karma-minaintyg.conf.ci.js',
                reporters: [ 'mocha' ]
            },
            webcert: {
                configFile: 'src/test/resources/karma-webcert.conf.ci.js',
                reporters: [ 'mocha' ]
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/common/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/common/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/common/webcert/js/module.min.js',
                dest: DEST_DIR + 'webjars/common/webcert/js/module.min.js'
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/common/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/common/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/common/webcert/js/module.min.js',
                dest: DEST_DIR + 'webjars/common/webcert/js/module.min.js'
            }
        }
    });

    grunt.registerTask('default', [ 'concat', 'ngAnnotate', 'uglify' ]);
    grunt.registerTask('lint-minaintyg', [ 'jshint:minaintyg', 'csslint:minaintyg' ]);
    grunt.registerTask('lint-webcert', [ 'jshint:webcert', 'csslint:webcert' ]);
    grunt.registerTask('lint', [ 'jshint', 'csslint' ]);
    grunt.registerTask('test-minaintyg', [ 'karma:minaintyg' ]);
    grunt.registerTask('test-webcert', [ 'karma:webcert' ]);
    grunt.registerTask('test', [ 'karma' ]);
};
