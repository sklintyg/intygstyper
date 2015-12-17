module.exports = {
    sjukpenning: {
        cwd: 'src/main/resources/META-INF/resources/webjars/sjukpenning/webcert',
        src: ['**/*.html'],
        dest: 'src/main/resources/META-INF/resources/webjars/sjukpenning/webcert/templates.js',
        options:{
            module: 'sjukpenning',
            url: function(url) {
                return '/web/webjars/sjukpenning/webcert/' + url;
            }
        }
    }
};
