define([
    'angular',
    'webjars/ts-diabetes/minaintyg/js/controllers/ViewCertCtrl',
    'webjars/ts-diabetes/minaintyg/js/controllers/SendCertWizardCtrl'
], function(angular, ViewCertCtrl, SendCertWizardCtrl) {
    'use strict';

    var moduleName = 'ts-diabetes.controllers';

    angular.module(moduleName, []).
        controller('ts-diabetes.ViewCertCtrl', ViewCertCtrl).
        controller('ts-diabetes.SendCertWizardCtrl', SendCertWizardCtrl);

    return moduleName;
});
