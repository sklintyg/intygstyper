module.exports = {
    webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/lisu/webcert',
        src: ['**/*.html'],
        dest: 'build/resources/main/META-INF/resources/webjars/lisu/webcert/templates.js',
        options:{
            module: 'lisu',
            url: function(url) {
                return '/web/webjars/lisu/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/lisu/minaintyg',
        src: ['**/*.html'],
        dest: 'build/resources/main/META-INF/resources/webjars/lisu/minaintyg/templates.js',
        options:{
            module: 'lisu',
            url: function(url) {
                return '/web/webjars/lisu/minaintyg/' + url;
            }
        }
    }
};
