define([
    'angular',
    'ts-bas/intyg/js/filters/BoolToTextFilter'
], function (angular, BoolToTextFilter) {
    'use strict';

    var moduleName = 'ts-bas.filters';

    angular.module(moduleName, [])
        .filter('BoolToTextFilter', BoolToTextFilter);

    return moduleName;
});
