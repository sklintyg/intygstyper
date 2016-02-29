/* global module */
function config(name) {
    return require('./tasks/' + name);
}

module.exports = function(grunt) {
    'use strict';

    grunt.loadNpmTasks('grunt-contrib-csslint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-angular-templates');
    grunt.loadNpmTasks('grunt-html2js');

    var SRC_DIR = 'src/main/resources/META-INF/resources/';
    var TEST_DIR = 'src/test/js/';
    var DEST_DIR = 'target/classes/META-INF/resources/';

    var minaintyg = grunt.file.expand({cwd:SRC_DIR}, ['webjars/luse/minaintyg/**/*.js', '!**/*.spec.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/luse/minaintyg/js/module-deps.json', JSON.stringify(minaintyg.
        map(function(file){ return '/web/'+file; }).
        concat('/web/webjars/luse/minaintyg/templates.js'), null, 4));
    minaintyg = [SRC_DIR + 'webjars/luse/minaintyg/js/module.js', DEST_DIR + 'webjars/luse/minaintyg/templates.js'].concat(minaintyg.map(function(file){
        return SRC_DIR + file;
    }));

    var webcert = grunt.file.expand({cwd:SRC_DIR}, ['webjars/luse/webcert/**/*.js', '!**/*.spec.js', '!**/*.test.js', '!**/module.js']).sort();
    grunt.file.write(DEST_DIR + 'webjars/luse/webcert/module-deps.json', JSON.stringify(webcert.
        map(function(file){ return '/web/'+file; }).
        concat('/web/webjars/luse/webcert/templates.js'), null, 4));
    webcert = [SRC_DIR + 'webjars/luse/webcert/module.js', DEST_DIR + 'webjars/luse/webcert/templates.js'].concat(webcert.map(function(file){
        return SRC_DIR + file;
    }));

    grunt.initConfig({

        csslint: {
            options: {
                csslintrc: '../target/build-tools/csslint/.csslintrc',
                force: true
            },
            minaintyg: {
                src: [ SRC_DIR + 'webjars/luse/minaintyg/**/*.css' ]
            },
            webcert: {
                src: [ SRC_DIR + 'webjars/luse/webcert/**/*.css' ]
            }
        },

        concat: {
            minaintyg: {
                src: minaintyg,
                dest: DEST_DIR + 'webjars/luse/minaintyg/js/module.min.js'
            },
            webcert: {
                src: webcert,
                dest: DEST_DIR + 'webjars/luse/webcert/module.min.js'
            }
        },

        jshint: {
            options: {
                jshintrc: '../target/build-tools/jshint/.jshintrc',
                force: true
            },
            minaintyg: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/luse/minaintyg/**/*.js', TEST_DIR + 'minaintyg/**/*.js' ]
            },
            webcert: {
                src: [ 'Gruntfile.js', SRC_DIR + 'webjars/luse/webcert/**/*.js', TEST_DIR + 'webcert/**/*.js' ]
            }
        },

        karma: {
            minaintyg: {
                configFile: 'src/main/resources/META-INF/resources/webjars/luse/karma-minaintyg.conf.ci.js',
                reporters: [ 'mocha' ]
            },
            webcert: {
                configFile: 'src/main/resources/META-INF/resources/webjars/luse/karma-webcert.conf.ci.js',
                reporters: [ 'mocha' ]
            }
        },

        ngAnnotate: {
            options: {
                singleQuotes: true
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/luse/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/luse/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/luse/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/luse/webcert/module.min.js'
            }
        },

        uglify: {
            options: {
                mangle: false
            },
            minaintyg: {
                src: DEST_DIR + 'webjars/luse/minaintyg/js/module.min.js',
                dest: DEST_DIR + 'webjars/luse/minaintyg/js/module.min.js'
            },
            webcert: {
                src: DEST_DIR + 'webjars/luse/webcert/module.min.js',
                dest: DEST_DIR + 'webjars/luse/webcert/module.min.js'
            }
        },

        ngtemplates: config('ngtemplates')


    });

    grunt.registerTask('default', [ 'ngtemplates', 'concat', 'ngAnnotate', 'uglify' ]);
    grunt.registerTask('lint-minaintyg', [ 'jshint:minaintyg', 'csslint:minaintyg' ]);
    grunt.registerTask('lint-webcert', [ 'jshint:webcert', 'csslint:webcert' ]);
    grunt.registerTask('lint', [ 'jshint', 'csslint' ]);
    grunt.registerTask('test-minaintyg', [ 'karma:minaintyg' ]);
    grunt.registerTask('test-webcert', [ 'karma:webcert' ]);
    grunt.registerTask('test', [ 'karma' ]);
};
