define([
    'angular',
    'ts-bas/intyg/js/controllers/ViewCertCtrl',
    'ts-bas/intyg/js/controllers/SendCertWizardCtrl'
], function(angular, ViewCertCtrl, SendCertWizardCtrl) {
    'use strict';

    var moduleName = 'ts-bas.controllers';

    angular.module(moduleName, []).
        controller('ts-bas.ViewCertCtrl', ViewCertCtrl).
        controller('ts-bas.SendCertWizardCtrl', SendCertWizardCtrl);

    return moduleName;
});
