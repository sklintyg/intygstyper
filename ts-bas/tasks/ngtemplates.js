/**
 * Created by stephenwhite on 27/03/15.
 */
module.exports = {
    tsbas: {
        cwd: 'src/main/resources/META-INF/resources/webjars/ts-bas/webcert',
        src: ['**/*.html'],
        dest: 'src/main/resources/META-INF/resources/webjars/ts-bas/webcert/js/templates.js',
        options:{
            module: 'ts-bas',
            url: function(url) {
                return '/web/webjars/ts-bas/webcert/' + url;
            }
        }
    }
};

