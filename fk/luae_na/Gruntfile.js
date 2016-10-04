/* global module */
function config(name) {
    'use strict';
    return require('./tasks/' + name);
}

module.exports = function(grunt) {
    'use strict';

    var npmDir = grunt.option('npmDir');
    var cwd = process.cwd();
    process.chdir(npmDir);

    require('time-grunt')(grunt);
    require('jit-grunt')(grunt, {
        bower: 'grunt-bower-task',
        ngtemplates: 'grunt-angular-templates'
    });

    process.chdir(cwd);

    var SRC_DIR = 'src/main/resources/META-INF/resources/';
    var TEST_DIR = 'src/test/js/';
    var DEST_DIR = (grunt.option('outputDir') || 'build/') +  'resources/main/META-INF/resources/';
    var TEST_OUTPUT_DIR = (grunt.option('outputDir') || 'build/karma/');
    var SKIP_COVERAGE = grunt.option('skip-coverage') !== undefined ? grunt.option('skip-coverage') : true;

    var minaintyg = grunt.file.expand({cwd:SRC_DIR}, ['webjars/luae_na/minaintyg/**/*.js', '!**/*.spec.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/luae_na/minaintyg/js/module-deps.json', JSON.stringify(minaintyg.
        map(function(file){ return '/web/'+file; }).
        concat('/web/webjars/luae_na/minaintyg/templates.js'), null, 4));
    minaintyg = [SRC_DIR + 'webjars/luae_na/minaintyg/js/module.js', DEST_DIR + 'webjars/luae_na/minaintyg/templates.js'].concat(minaintyg.map(function(file){
        return SRC_DIR + file;
    }));

    var webcert = grunt.file.expand({cwd:SRC_DIR}, ['webjars/luae_na/webcert/**/*.js', '!**/*.spec.js', '!**/*.test.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/luae_na/webcert/module-deps.json', JSON.stringify(webcert.
        map(function(file){ return '/web/'+file; }).
        concat('/web/webjars/luae_na/webcert/templates.js'), null, 4));
    webcert = [SRC_DIR + 'webjars/luae_na/webcert/module.js', DEST_DIR + 'webjars/luae_na/webcert/templates.js'].concat(webcert.map(function(file){
        return SRC_DIR + file;
    }));

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
                dest: DEST_DIR + 'webjars/luae_na/minaintyg/js/module.min.js'
            },
            webcert: {
                src: webcert,
                dest: DEST_DIR + 'webjars/luae_na/webcert/module.min.js'
            }
        },

        jshint: {
            options: {
                jshintrc: 'build/build-tools/jshint/.jshintrc',
                reporterOutput: '',
                force: false
            },
            minaintyg: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/luae_na/minaintyg/**/*.js', TEST_DIR + 'minaintyg/**/*.js' ]
            },
            webcert: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/luae_na/webcert/**/*.js', TEST_DIR + 'webcert/**/*.js' ]
            }
        },

        karma: {
            minaintyg: {
                configFile: SRC_DIR + 'webjars/luae_na/karma-minaintyg.conf.ci.js',
                coverageReporter: {
                    type : 'lcovonly',
                    dir : TEST_OUTPUT_DIR + 'minaintyg/',
                    subdir: '.'
                }
            },
            webcert: {
                configFile: SRC_DIR + 'webjars/luae_na/karma-webcert.conf.ci.js',
                coverageReporter: {
                    type : 'lcovonly',
                    dir : TEST_OUTPUT_DIR + 'webcert/',
                    subdir: '.'
                }
            }
        },

        // Compiles Sass to CSS
        sass: {
            options: {
            },
            dist: {
                files: [{
                    expand: true,
                    cwd: SRC_DIR + 'webjars/luae_na/webcert/css/',
                    src: ['*.scss'],
                    dest: DEST_DIR + 'webjars/luae_na/webcert/css',
                    ext: '.css'
                },
                    {
                        expand: true,
                        cwd: SRC_DIR + 'webjars/luae_na/minaintyg/css/',
                        src: ['*.scss'],
                        dest: DEST_DIR + 'webjars/luae_na/minaintyg/css',
                        ext: '.css'
                    }]
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/luae_na/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/luae_na/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/luae_na/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/luae_na/webcert/module.min.js'
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/luae_na/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/luae_na/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/luae_na/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/luae_na/webcert/module.min.js'
            }
        },

        ngtemplates: config('ngtemplates'),

        lcovMerge: {
            options: {
                outputFile: TEST_OUTPUT_DIR + 'merged_lcov.info'
            },
            src: [TEST_OUTPUT_DIR + 'webcert/*.info', TEST_OUTPUT_DIR +'minaintyg/*.info']
        }

    });

    grunt.registerTask('default', [ 'ngtemplates', 'concat', 'ngAnnotate', 'uglify', 'sass', 'jshint' ]);
    grunt.registerTask('lint-minaintyg', [ 'jshint:minaintyg' ]);
    grunt.registerTask('lint-webcert', [ 'jshint:webcert' ]);
    grunt.registerTask('lint', [ 'jshint', 'sasslint' ]);
    grunt.registerTask('test-minaintyg', [ 'karma:minaintyg' ]);
    grunt.registerTask('test-webcert', [ 'karma:webcert' ]);
    grunt.registerTask('test', [ 'karma' ].concat(SKIP_COVERAGE?[]:['lcovMerge']));
};
