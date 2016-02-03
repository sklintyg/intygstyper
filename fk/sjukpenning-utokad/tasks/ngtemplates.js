module.exports = {
    webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/sjukpenning-utokad/webcert',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/sjukpenning-utokad/webcert/templates.js',
        options:{
            module: 'lisu',
            url: function(url) {
                return '/web/webjars/sjukpenning-utokad/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/sjukpenning-utokad/minaintyg',
        src: ['**/*.html'],
        dest: 'target/classes/resources/META-INF/resources/webjars/sjukpenning-utokad/minaintyg/templates.js',
        options:{
            module: 'lisu',
            url: function(url) {
                return '/web/webjars/sjukpenning-utokad/minaintyg/' + url;
            }
        }
    }
};
