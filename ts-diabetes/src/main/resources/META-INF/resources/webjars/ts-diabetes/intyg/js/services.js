define([
    'angular',
    'ts-diabetes/intyg/js/services/certificateService'
], function (angular, certificateService) {
    'use strict';

    var moduleName = "ts-diabetes.services";

    angular.module(moduleName, [])
        .factory("ts-diabetes.certificateService", certificateService);

    return moduleName;
});
