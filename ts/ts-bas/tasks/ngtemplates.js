/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
