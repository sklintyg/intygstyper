module.exports = {
    sjukersattning: {
        cwd: 'src/main/resources/META-INF/resources/webjars/sjukersattning/webcert',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/sjukersattning/webcert/templates.js',
        options:{
            module: 'sjukersattning',
            url: function(url) {
                return '/web/webjars/sjukersattning/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/sjukersattning/minaintyg',
        src: ['**/*.html'],
        dest: 'target/classes/resources/META-INF/resources/webjars/sjukersattning/minaintyg/templates.js',
        options:{
            module: 'sjukersattning',
            url: function(url) {
                return '/web/webjars/sjukersattning/minaintyg/' + url;
            }
        }
    }
};
