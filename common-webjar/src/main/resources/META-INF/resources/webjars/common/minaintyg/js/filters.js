define([
    'angular',
    'webjars/common/minaintyg/js/filters/BoolToTextFilter'
], function (angular, BoolToTextFilter) {
    'use strict';

    var moduleName = 'common.filters';

    angular.module(moduleName, [])
        .filter('BoolToTextFilter', BoolToTextFilter);

    return moduleName;
});
