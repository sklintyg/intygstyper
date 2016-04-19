module.exports = {
    webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/luaefs/webcert',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/luaefs/webcert/templates.js',
        options:{
            module: 'luaefs',
            url: function(url) {
                return '/web/webjars/luaefs/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/luaefs/minaintyg',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/luaefs/minaintyg/templates.js',
        options:{
            module: 'luaefs',
            url: function(url) {
                return '/web/webjars/luaefs/minaintyg/' + url;
            }
        }
    }
};
