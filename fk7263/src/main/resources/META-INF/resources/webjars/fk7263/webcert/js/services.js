define([
    'angular',
    'fk7263/common/js/services/certificateService'
], function (angular, certificateService) {
    'use strict';

    var moduleName = "fk7263.services";

    angular.module(moduleName, [])
        .factory("fk7263.certificateService", certificateService);

    return moduleName;
});
