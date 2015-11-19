/* global module */
function config(name) {
    'use strict';
    return require('./tasks/' + name);
}

module.exports = function(grunt) {
    'use strict';

    grunt.loadNpmTasks('grunt-contrib-csslint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-lcov-merge');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-angular-templates');
    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-sass-lint');

    var SRC_DIR = 'src/main/resources/META-INF/resources/';
    var TEST_DIR = 'src/test/js/';
    var DEST_DIR = 'target/classes/META-INF/resources/';

    var minaintyg = grunt.file.readJSON(SRC_DIR + 'webjars/ts-bas/minaintyg/js/module-deps.json').map(function(file) {
        return file.replace(/\/web\//g, SRC_DIR);
    });
    minaintyg = [SRC_DIR + 'webjars/ts-bas/minaintyg/js/module.js'].concat(minaintyg);

    var webcert = grunt.file.readJSON(SRC_DIR + 'webjars/ts-bas/webcert/module-deps.json').map(function(file) {
        return file.replace(/\/web\//g, SRC_DIR);
    });
    webcert = [SRC_DIR + 'webjars/ts-bas/webcert/module.js'].concat(webcert);

    grunt.initConfig({
        
        sasslint: {
            options: {
                //configFile: 'config/.sass-lint.yml' //For now we use the .sass-lint.yml that is packaged with sass-lint
            },
            target: [SRC_DIR + '**/*.scss']
        },

        concat: {
            minaintyg: {
                src: minaintyg,
                dest: DEST_DIR + 'webjars/ts-bas/minaintyg/js/module.min.js'
            },
            webcert: {
                src: webcert,
                dest: DEST_DIR + 'webjars/ts-bas/webcert/module.min.js'
            }
        },

        jshint: {
            options: {
                jshintrc: 'target/build-tools/jshint/.jshintrc',
                force: false,
                ignores: ['**/templates.js']
            },
            minaintyg: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/ts-bas/minaintyg/**/*.js', TEST_DIR + 'minaintyg/**/*.js' ]
            },
            webcert: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/ts-bas/webcert/**/*.js', TEST_DIR + 'webcert/**/*.js' ]
            }
        },

        karma: {
            minaintyg: {
                configFile: 'src/main/resources/META-INF/resources/webjars/ts-bas/karma-minaintyg.conf.ci.js',
                reporters: [ 'mocha' ]
            },
            webcert: {
                configFile: 'src/main/resources/META-INF/resources/webjars/ts-bas/karma-webcert.conf.ci.js',
                reporters: [ 'mocha' ]
            }
        },

        // Compiles Sass to CSS
        sass: {
            options: {
            },
            dist: {
                files: [{
                    expand: true,
                    cwd: SRC_DIR + 'webjars/ts-bas/webcert/css/',
                    src: ['*.scss'],
                    dest: DEST_DIR + 'webjars/ts-bas/webcert/css',
                    ext: '.css'
                }, 
                {
                    expand: true,
                    cwd: SRC_DIR + 'webjars/ts-bas/minaintyg/css/',
                    src: ['*.scss'],
                    dest: DEST_DIR + 'webjars/ts-bas/minaintyg/css',
                    ext: '.css'
                }]
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/ts-bas/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/ts-bas/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/ts-bas/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/ts-bas/webcert/module.min.js'
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/ts-bas/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/ts-bas/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/ts-bas/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/ts-bas/webcert/module.min.js'
            }
        },

        ngtemplates: config('ngtemplates'),

        lcovMerge: {
            options: {
                outputFile: 'target/karma_coverage/merged_lcov.info'
            },
            src: ['target/karma_coverage/webcert/*.info', 'target/karma_coverage/minaintyg/*.info']
        }
    });

    grunt.registerTask('default', [ 'ngtemplates','concat', 'ngAnnotate', 'uglify', 'sass' ]);
    grunt.registerTask('lint-minaintyg', [ 'jshint:minaintyg' ]);
    grunt.registerTask('lint-webcert', [ 'jshint:webcert' ]);
    grunt.registerTask('lint', [ 'jshint', 'sasslint' ]);
    grunt.registerTask('test-minaintyg', [ 'karma:minaintyg' ]);
    grunt.registerTask('test-webcert', [ 'karma:webcert' ]);
    grunt.registerTask('test', [ 'karma' ]);
};
