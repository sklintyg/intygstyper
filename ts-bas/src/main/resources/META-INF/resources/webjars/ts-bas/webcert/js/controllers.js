define([
    'angular',
    'ts-bas/webcert/js/services',
    'ts-bas/webcert/js/controllers/EditCertCtrl',
    'ts-bas/common/js/controllers/ViewCertCtrl',
    'ts-bas/webcert/js/controllers/TestCtrl'
], function (angular, services, EditCertCtrl, ViewCertCtrl, TestCtrl) {
    'use strict';

    var moduleName = "ts-bas.controllers";

    angular.module(moduleName, [services])
        .controller('ts-bas.EditCertCtrl', EditCertCtrl)
        .controller('ts-bas.ViewCertCtrl', ViewCertCtrl)
        .controller('ts-bas.TestCtrl', TestCtrl);

    return moduleName;
});
