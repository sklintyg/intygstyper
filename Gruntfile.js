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
    var RUN_COVERAGE = grunt.option('run-coverage') !== undefined ? grunt.option('run-coverage') : false;
    var MODULE = grunt.option('module');

    var minaintyg = grunt.file.expand({cwd:SRC_DIR}, ['webjars/' + MODULE + '/minaintyg/**/*.js', '!**/*.spec.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module-deps.json',
                     JSON.stringify(minaintyg.map(function(file){ return '/web/'+file; }).concat('/web/webjars/' + MODULE + '/minaintyg/templates.js'), null, 4));
    minaintyg = [SRC_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.js', DEST_DIR + 'webjars/' + MODULE + '/minaintyg/templates.js'].concat(minaintyg.map(function(file){
        return SRC_DIR + file;
    }));

    var webcert = grunt.file.expand({cwd:SRC_DIR}, ['webjars/' + MODULE + '/webcert/**/*.js', '!**/*.spec.js', '!**/*.test.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/' + MODULE + '/webcert/module-deps.json',
                     JSON.stringify(webcert.map(function(file){ return '/web/'+file; }).concat('/web/webjars/' + MODULE + '/webcert/templates.js'), null, 4));
    webcert = [SRC_DIR + 'webjars/' + MODULE + '/webcert/module.js', DEST_DIR + 'webjars/' + MODULE + '/webcert/templates.js'].concat(webcert.map(function(file){
        return SRC_DIR + file;
    }));

    grunt.initConfig({

        bower: {
            install: {
                options: {
                    copy: false
                }
            }
        },

        wiredep: {
            options: {
                devDependencies: true
            },
            webcert: {
                src: ['karma-webcert.conf.js']
            },
            minaintyg: {
                src: ['karma-minaintyg.conf.js']
            }
        },

        sasslint: {
            options: {
                //configFile: 'config/.sass-lint.yml' //For now we use the .sass-lint.yml that is packaged with sass-lint
            },
            target: [SRC_DIR + '**/*.scss']
        },

        concat: {
            minaintyg: {
                src: minaintyg,
                dest: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.min.js'
            },
            webcert: {
                src: webcert,
                dest: DEST_DIR + 'webjars/' + MODULE + '/webcert/module.min.js'
            }
        },

        jshint: {
            options: {
                jshintrc: 'build/build-tools/jshint/.jshintrc',
                reporterOutput: '',
                force: false
            },
            minaintyg: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/' + MODULE + '/minaintyg/**/*.js', TEST_DIR + 'minaintyg/**/*.js' ]
            },
            webcert: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/' + MODULE + '/webcert/**/*.js', TEST_DIR + 'webcert/**/*.js' ]
            }
        },

        karma: {
            minaintyg: {
                configFile: 'karma-minaintyg.conf.ci.js',
                coverageReporter: {
                    type : 'lcovonly',
                    dir : TEST_OUTPUT_DIR + 'minaintyg/',
                    subdir: '.'
                }
            },
            webcert: {
                configFile: 'karma-webcert.conf.ci.js',
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
                files: [
                    {
                        expand: true,
                        cwd: SRC_DIR + 'webjars/' + MODULE + '/webcert/css/',
                        src: ['*.scss'],
                        dest: DEST_DIR + 'webjars/' + MODULE + '/webcert/css',
                        ext: '.css'
                    }, 
                    {
                        expand: true,
                        cwd: SRC_DIR + 'webjars/' + MODULE + '/minaintyg/css/',
                        src: ['*.scss'],
                        dest: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/css',
                        ext: '.css'
                    }]
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/' + MODULE + '/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/' + MODULE + '/webcert/module.min.js'
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/' + MODULE + '/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/' + MODULE + '/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/' + MODULE + '/webcert/module.min.js'
            }
        },

        lcovMerge: {
            options: {
                outputFile: TEST_OUTPUT_DIR + 'merged_lcov.info'
            },
            src: [TEST_OUTPUT_DIR + 'webcert/*.info', TEST_OUTPUT_DIR +'minaintyg/*.info']
        },

        ngtemplates : {
            webcert: {
                cwd: 'src/main/resources/META-INF/resources/webjars/' + MODULE + '/webcert',
                src: ['**/*.html'],
                dest: 'build/resources/main/META-INF/resources/webjars/' + MODULE + '/webcert/templates.js',
                options:{
                    module: MODULE,
                    url: function(url) {
                        return '/web/webjars/' + MODULE + '/webcert/' + url;
                    }
                }
            },
            minaintyg: {
                cwd: 'src/main/resources/META-INF/resources/webjars/' + MODULE + '/minaintyg',
                src: ['**/*.html'],
                dest: 'build/resources/main/META-INF/resources/webjars/' + MODULE + '/minaintyg/templates.js',
                options:{
                    module: MODULE,
                    url: function(url) {
                        return '/web/webjars/' + MODULE + '/minaintyg/' + url;
                    }
                }
            }
        }
    });

    grunt.registerTask('default', [ 'ngtemplates', 'concat', 'ngAnnotate', 'uglify', 'sass' ]);
    grunt.registerTask('lint-minaintyg', [ 'jshint:minaintyg' ]);
    grunt.registerTask('lint-webcert', [ 'jshint:webcert' ]);
    grunt.registerTask('lint', [ 'jshint' ]);
    grunt.registerTask('test-minaintyg', [ 'bower:minaintyg', 'wiredep:minaintyg', 'karma:minaintyg' ]);
    grunt.registerTask('test-webcert', [ 'bower:webcert', 'wiredep:webcert', 'karma:webcert' ]);
    grunt.registerTask('test', [ 'bower', 'wiredep', 'karma' ].concat(RUN_COVERAGE?['lcovMerge']:[]));
};
