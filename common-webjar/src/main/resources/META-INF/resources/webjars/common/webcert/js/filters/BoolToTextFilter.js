define([
    'angular'
], function(angular) {
    'use strict';

    var moduleName = 'BoolToTextFilter';

    angular.module(moduleName, []).
        filter(moduleName, function() {
            return function(input) {
                return input === true ? 'common.yes' : 'common.no';
            };
        });

    return moduleName;
});
