define([ 'angular' ], function() {
    'use strict';

    return function() {
        return function(input) {
            return input === true ? 'ts-diabetes.label.true' : 'ts-diabetes.label.false';
        };
    };
});
