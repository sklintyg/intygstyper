define([
    'angular',
    'ts-bas/common/js/directives/eyeDecimal'
], function (angular, eyeDecimal) {
    'use strict';

    var moduleName = "ts-bas.directives";

    angular.module(moduleName, [])
        .directive("ts-bas.eyeDecimal", eyeDecimal);

    return moduleName;
});