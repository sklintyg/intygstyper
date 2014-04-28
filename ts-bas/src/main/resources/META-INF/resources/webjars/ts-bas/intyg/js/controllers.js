define([
    'angular',
    'ts-bas/intyg/js/services',
    'ts-bas/intyg/js/controllers/ViewCertCtrl',
    'ts-bas/intyg/js/controllers/SendCertWizardCtrl'
], function (angular, services, ViewCertCtrl, SendCertWizardCtrl) {
    'use strict';

    var moduleName = "ts-bas.controllers";

    angular.module(moduleName, [services])
        .controller('ts-bas.ViewCertCtrl', ViewCertCtrl)
        .controller('ts-bas.SendCertWizardCtrl', SendCertWizardCtrl);

    return moduleName;
});