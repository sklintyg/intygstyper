/**
 * Created by stephenwhite on 27/03/15.
 */
module.exports = {
    webcert: {
        cwd: 'src/main/resources/META-INF/resources/webjars/ts-diabetes/webcert',
        src: ['**/*.html'],
        dest: 'src/main/resources/META-INF/resources/webjars/ts-diabetes/webcert/templates.js',
        options:{
            module: 'ts-diabetes',
            url: function(url) {
                return '/web/webjars/ts-diabetes/webcert/' + url;
            }
        }
    },
    minaintyg: {
        cwd: 'src/main/resources/META-INF/resources/webjars/ts-diabetes/minaintyg',
        src: ['**/*.html'],
        dest: 'src/main/resources/META-INF/resources/webjars/ts-diabetes/minaintyg/templates.js',
        options:{
            module: 'ts-diabetes',
            url: function(url) {
                return '/web/webjars/ts-diabetes/minaintyg/' + url;
            }
        }
    }
};

