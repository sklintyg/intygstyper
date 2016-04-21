module.exports = {
    webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/luae_fs/webcert',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/luae_fs/webcert/templates.js',
        options:{
            module: 'luae_fs',
            url: function(url) {
                return '/web/webjars/luae_fs/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/luae_fs/minaintyg',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/luae_fs/minaintyg/templates.js',
        options:{
            module: 'luae_fs',
            url: function(url) {
                return '/web/webjars/luae_fs/minaintyg/' + url;
            }
        }
    }
};
