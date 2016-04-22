module.exports = {
    webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/luae_na/webcert',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/luae_na/webcert/templates.js',
        options:{
            module: 'luae_na',
            url: function(url) {
                return '/web/webjars/luae_na/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/luae_na/minaintyg',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/luae_na/minaintyg/templates.js',
        options:{
            module: 'luae_na',
            url: function(url) {
                return '/web/webjars/luae_na/minaintyg/' + url;
            }
        }
    }
};
