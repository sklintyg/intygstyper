define([
    'angular',
    'ts-diabetes/common/js/directives/eyeDecimal'
], function (angular, eyeDecimal) {
    'use strict';

    var moduleName = "ts-diabetes.directives";

    angular.module(moduleName, [])
        .directive("ts-diabetes.eyeDecimal", eyeDecimal);

    return moduleName;
});