define([
    'angular',
    'common/minaintyg/js/CertificateService',
    'webjars/ts-diabetes/minaintyg/js/controllers/ViewCertCtrl',
    'common/minaintyg/js/controllers/SendCertWizardCtrl'
], function(angular, CertificateService, ViewCertCtrl, SendCertWizardCtrl) {
    'use strict';

    var moduleName = 'ts-diabetes.controllers';

    angular.module(moduleName, [ CertificateService ]).
        controller('ts-diabetes.ViewCertCtrl', ViewCertCtrl).
        controller('ts-diabetes.SendCertWizardCtrl', SendCertWizardCtrl);

    return moduleName;
});
