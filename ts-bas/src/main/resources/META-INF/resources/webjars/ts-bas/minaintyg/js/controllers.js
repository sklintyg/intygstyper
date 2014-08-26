define([
    'angular',
    'common/minaintyg/js/CertificateService',
    'ts-bas/minaintyg/js/controllers/ViewCertCtrl',
    'common/minaintyg/js/controllers/SendCertWizardCtrl'
], function(angular, CertificateService, ViewCertCtrl, SendCertWizardCtrl) {
    'use strict';

    var moduleName = 'ts-bas.controllers';

    angular.module(moduleName, [ CertificateService ]).
        controller('ts-bas.ViewCertCtrl', ViewCertCtrl).
        controller('ts-bas.SendCertWizardCtrl', SendCertWizardCtrl);

    return moduleName;
});
