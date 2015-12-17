/**
 * Created by stephenwhite on 27/03/15.
 */
module.exports = {
    webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/ts-bas/webcert',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/ts-bas/webcert/templates.js',
        options:{
            module: 'ts-bas',
            url: function(url) {
                return '/web/webjars/ts-bas/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/ts-bas/minaintyg',
        src: ['**/*.html'],
        dest: 'target/classes/META-INF/resources/webjars/ts-bas/minaintyg/templates.js',
        options:{
            module: 'ts-bas',
            url: function(url) {
                return '/web/webjars/ts-bas/minaintyg/' + url;
            }
        }
    }
};

