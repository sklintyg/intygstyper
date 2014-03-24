define([
    'angular',
    'rli/webcert/js/services',
    'rli/webcert/js/controllers/EditCertCtrl',
    'rli/common/js/controllers/ViewCertCtrl'
], function (angular, services, EditCertCtrl, ViewCertCtrl) {
    'use strict';

    var moduleName = "rli.controllers";

    angular.module(moduleName, [services])
        .controller('rli.EditCertCtrl', EditCertCtrl)
        .controller('rli.ViewCertCtrl', ViewCertCtrl);

    return moduleName;
});
