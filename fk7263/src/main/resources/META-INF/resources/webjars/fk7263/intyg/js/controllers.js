define([
    'angular',
    'fk7263/intyg/js/services',
    'fk7263/intyg/js/controllers/SentCertWizardCtrl',
    'fk7263/intyg/js/controllers/ErrorCtrl',
    'fk7263/intyg/js/controllers/ViewCertCtrl'
], function (angular, services, SentCertWizardCtrl, ErrorCtrl, ViewCertCtrl) {
    'use strict';

    var moduleName = "fk7263.controllers";

    angular.module(moduleName, [services])
        .controller('SentCertWizardCtrl', SentCertWizardCtrl)
        .controller('ErrorCtrl', ErrorCtrl)
        .controller('ViewCertCtrl', ViewCertCtrl);

    return moduleName;
});
