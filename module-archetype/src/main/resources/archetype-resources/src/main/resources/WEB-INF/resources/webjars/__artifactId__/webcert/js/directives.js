#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
define([
    'angular',
    '${artifactId}/common/js/directives/eyeDecimal'
], function (angular, eyeDecimal) {
    'use strict';

    var moduleName = "${artifactId}.directives";

    angular.module(moduleName, [])
        .directive("${artifactId}.eyeDecimal", eyeDecimal);

    return moduleName;
});