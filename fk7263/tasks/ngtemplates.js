/**
 * Created by stephenwhite on 27/03/15.
 */
module.exports = {
    webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/fk7263/webcert',
        src: ['**/*.html'],
        dest: 'src/main/resources/META-INF/resources/webjars/fk7263/webcert/templates.js',
        options:{
            module: 'fk7263',
            url: function(url) {
                return '/web/webjars/fk7263/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/fk7263/minaintyg',
        src: ['**/*.html'],
        dest: 'src/main/resources/META-INF/resources/webjars/fk7263/minaintyg/templates.js',
        options:{
            module: 'fk7263',
            url: function(url) {
                return '/web/webjars/fk7263/minaintyg/' + url;
            }
        }
    }
};

