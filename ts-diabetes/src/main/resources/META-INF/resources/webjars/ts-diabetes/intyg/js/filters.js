define([
    'angular',
    'ts-diabetes/intyg/js/filters/BoolToTextFilter'
], function (angular, BoolToTextFilter) {
    'use strict';

    var moduleName = 'ts-diabetes.filters';

    angular.module(moduleName, [])
        .filter('BoolToTextFilter', BoolToTextFilter);

    return moduleName;
});
