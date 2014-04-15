define([
    'angular'
], function (angular) {
    'use strict';
    return function () {
        return function (input) {
            return input == true ? 'ts-bas.label.true' : 'ts-bas.label.false';
        };
    };
});
