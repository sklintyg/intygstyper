module.exports = {
    webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/luse/webcert',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/luse/webcert/templates.js',
        options:{
            module: 'luse',
            url: function(url) {
                return '/web/webjars/luse/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/luse/minaintyg',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/luse/minaintyg/templates.js',
        options:{
            module: 'luse',
            url: function(url) {
                return '/web/webjars/luse/minaintyg/' + url;
            }
        }
    }
};
