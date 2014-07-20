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

    var webcert = grunt.file.readJSON(SRC_DIR + 'webjars/ts-diabetes/webcert/js/module-deps.json').map(function(file) {
        return file.replace(/\/web\//g, SRC_DIR);
    });
    webcert = [SRC_DIR + 'webjars/ts-diabetes/webcert/js/module.js'].concat(webcert);

    grunt.initConfig({

        csslint: {
            dev: {
                options: {
                    csslintrc: '../src/main/resources/.csslintrc',
                    force: true
                },
                src: [ SRC_DIR + 'webjars/ts-diabetes/**/*.css' ]
            }
        },

        concat: {
            webcert: {
                src: webcert,
                dest: DEST_DIR + 'webjars/ts-diabetes/webcert/js/module.min.js'
            }
        },

        jshint: {
            dev: {
                options: {
                    jshintrc: '../src/main/resources/.jshintrc',
                    force: true
                },
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/ts-diabetes/**/*.js', TEST_DIR + '**/*.js' ]
            }
        },

        karma: {
            unit: {
                configFile: 'src/test/resources/karma.conf.ci.js'
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            webcert: {
                src: DEST_DIR + 'webjars/ts-diabetes/webcert/js/module.min.js',
                dest: DEST_DIR + 'webjars/ts-diabetes/webcert/js/module.min.js'
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            webcert: {
                src: DEST_DIR + 'webjars/ts-diabetes/webcert/js/module.min.js',
                dest: DEST_DIR + 'webjars/ts-diabetes/webcert/js/module.min.js'
            }
        }
    });

    grunt.registerTask('default', [ 'concat', 'ngAnnotate', 'uglify' ]);
    grunt.registerTask('lint', [ 'jshint', 'csslint' ]);
    grunt.registerTask('test', [ 'karma' ]);
};
