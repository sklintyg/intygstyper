define([
    'angular',
    'ts-bas/minaintyg/js/controllers/ViewCertCtrl',
    'ts-bas/minaintyg/js/controllers/SendCertWizardCtrl'
], function(angular, ViewCertCtrl, SendCertWizardCtrl) {
    'use strict';

    var moduleName = 'ts-bas.controllers';

    angular.module(moduleName, []).
        controller('ts-bas.ViewCertCtrl', ViewCertCtrl).
        controller('ts-bas.SendCertWizardCtrl', SendCertWizardCtrl);

    return moduleName;
});
