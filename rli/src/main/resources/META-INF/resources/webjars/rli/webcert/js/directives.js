define([
    'angular',
    'rli/common/js/directives/eyeDecimal'
], function(angular, eyeDecimal) {
    'use strict';

    var moduleName = 'rli.directives';

    angular.module(moduleName, []).
        directive('rli.eyeDecimal', eyeDecimal);

    return moduleName;
});
