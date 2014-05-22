define([ 'angular' ], function() {
    'use strict';

    return function() {
        return function(input) {
            return input === true ? 'common.yes' : 'common.no';
        };
    };
});
