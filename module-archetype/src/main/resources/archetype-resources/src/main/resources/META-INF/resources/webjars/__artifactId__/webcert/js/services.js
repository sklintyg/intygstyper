#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
define([
    'angular',
    '${artifactId}/common/js/services/certificateService'
], function (angular, certificateService) {
    'use strict';

    var moduleName = "${artifactId}.services";

    angular.module(moduleName, [])
        .factory("${artifactId}.certificateService", certificateService);

    return moduleName;
});
