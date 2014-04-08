define([
    'angular',
    'ts-bas/intyg/js/services',
    'ts-bas/intyg/js/controllers/ViewCertCtrl'
], function (angular, services, ViewCertCtrl) {
    'use strict';

    var moduleName = "ts-bas.controllers";

    angular.module(moduleName, [services])
        .controller('ViewCertCtrl', ViewCertCtrl);

    return moduleName;
});