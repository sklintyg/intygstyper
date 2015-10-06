module.exports = {
    sjukersattning: {
        cwd: 'src/main/resources/META-INF/resources/webjars/sjukersattning/webcert',
        src: ['**/*.html'],
        dest: 'src/main/resources/META-INF/resources/webjars/sjukersattning/webcert/templates.js',
        options:{
            module: 'sjukersattning',
            url: function(url) {
                return '/web/webjars/sjukersattning/webcert/' + url;
            }
        }
    }
};
