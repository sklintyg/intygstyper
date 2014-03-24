define([
    'angular',
    'rli/common/js/services/certificateService'
], function (angular, certificateService) {
    'use strict';

    var moduleName = "rli.services";

    angular.module(moduleName, [])
        .factory("rli.certificateService", certificateService);

    return moduleName;
});
