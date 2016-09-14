/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* global module */
function config(name) {
    'use strict';
    return require('./tasks/' + name);
}

module.exports = function(grunt) {
    'use strict';

    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-lcov-merge');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-angular-templates');
    grunt.loadNpmTasks('grunt-html2js');
    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-sass-lint');

    var SRC_DIR = 'src/main/resources/META-INF/resources/';
    var TEST_DIR = 'src/test/js/';
    var DEST_DIR = (grunt.option('outputDir') || 'build/') +  'resources/main/META-INF/resources/';
    var TEST_OUTPUT_DIR = (grunt.option('outputDir') || 'build/karma/');
    var SKIP_COVERAGE = grunt.option('skip-coverage') !== undefined ? grunt.option('skip-coverage') : true;

    var minaintyg = grunt.file.expand({cwd:SRC_DIR}, ['webjars/fk7263/minaintyg/**/*.js', '!**/*.spec.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/fk7263/minaintyg/js/module-deps.json', JSON.stringify(minaintyg.
        map(function(file){ return '/web/'+file; }).
        concat('/web/webjars/fk7263/minaintyg/templates.js'), null, 4));
    minaintyg = [SRC_DIR + 'webjars/fk7263/minaintyg/js/module.js', DEST_DIR + 'webjars/fk7263/minaintyg/templates.js'].concat(minaintyg.map(function(file){
        return SRC_DIR + file;
    }));

    var webcert = grunt.file.expand({cwd:SRC_DIR}, ['webjars/fk7263/webcert/**/*.js', '!**/*.spec.js', '!**/*.test.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/fk7263/webcert/module-deps.json', JSON.stringify(webcert.
        map(function(file){ return '/web/'+file; }).
        concat('/web/webjars/fk7263/webcert/templates.js'), null, 4));
    webcert = [SRC_DIR + 'webjars/fk7263/webcert/module.js', DEST_DIR + 'webjars/fk7263/webcert/templates.js'].concat(webcert.map(function(file){
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
                dest: DEST_DIR + 'webjars/fk7263/minaintyg/js/module.min.js'
            },
            webcert: {
                src: webcert,
                dest: DEST_DIR + 'webjars/fk7263/webcert/module.min.js'
            }
        },

        jshint: {
            options: {
                jshintrc: 'build/build-tools/jshint/.jshintrc',
                force: false,
                ignores: ['**/templates.js']
            },
            minaintyg: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/fk7263/minaintyg/**/*.js', TEST_DIR + 'minaintyg/**/*.js' ]
            },
            webcert: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/fk7263/webcert/**/*.js', TEST_DIR + 'webcert/**/*.js' ]
            }
        },

        karma: {
            minaintyg: {
                configFile: SRC_DIR + 'webjars/fk7263/karma-minaintyg.conf.ci.js',
                coverageReporter: {
                    type : 'lcovonly',
                    dir : TEST_OUTPUT_DIR + 'minaintyg/',
                    subdir: '.'
                }
            },
            webcert: {
                configFile: SRC_DIR + 'webjars/fk7263/karma-webcert.conf.ci.js',
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
                    cwd: SRC_DIR + 'webjars/fk7263/webcert/css/',
                    src: ['*.scss'],
                    dest: DEST_DIR + 'webjars/fk7263/webcert/css',
                    ext: '.css'
                }, 
                {
                    expand: true,
                    cwd: SRC_DIR + 'webjars/fk7263/minaintyg/css/',
                    src: ['*.scss'],
                    dest: DEST_DIR + 'webjars/fk7263/minaintyg/css',
                    ext: '.css'
                }]
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/fk7263/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/fk7263/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/fk7263/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/fk7263/webcert/module.min.js'
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/fk7263/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/fk7263/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/fk7263/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/fk7263/webcert/module.min.js'
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
