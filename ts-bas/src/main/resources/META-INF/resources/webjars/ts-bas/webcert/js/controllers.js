define([
    'angular',
    'ts-bas/webcert/js/services',
    'ts-bas/webcert/js/controllers/EditCertCtrl',
    'ts-bas/common/js/controllers/ViewCertCtrl'
], function (angular, services, EditCertCtrl, ViewCertCtrl) {
    'use strict';

    var moduleName = "ts-bas.controllers";

    angular.module(moduleName, [services])
        .controller('ts-bas.EditCertCtrl', EditCertCtrl)
        .controller('ts-bas.ViewCertCtrl', ViewCertCtrl);

    return moduleName;
});
