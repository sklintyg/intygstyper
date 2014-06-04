define([
    'angular',
    'webjars/common/webcert/js/filters/BoolToTextFilter'
], function(angular, BoolToTextFilter) {
    'use strict';

    var moduleName = 'common.filters';

    angular.module(moduleName, [ BoolToTextFilter ]);

    return moduleName;
});
