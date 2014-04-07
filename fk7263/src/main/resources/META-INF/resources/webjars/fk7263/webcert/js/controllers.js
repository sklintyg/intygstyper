define([
    'angular',
    'fk7263/webcert/js/services',
    'fk7263/webcert/js/controllers/EditCertCtrl',
    'fk7263/webcert/js/controllers/ViewCertCtrl',
    'fk7263/webcert/js/controllers/QACtrl'
], function (angular, services, EditCertCtrl, ViewCertCtrl, QACtrl) {
    'use strict';

    var moduleName = "fk7263.controllers";

    angular.module(moduleName, [services])
        .controller('fk7263.EditCertCtrl', EditCertCtrl)
        .controller('fk7263.ViewCertCtrl', ViewCertCtrl)
        .controller('fk7263.QACtrl', QACtrl);

    return moduleName;
});
