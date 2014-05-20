define(['angular',
        'ts-diabetes/intyg/js/controllers/ViewCertCtrl',
        'ts-diabetes/intyg/js/controllers/SendCertWizardCtrl' ],
    function(angular, ViewCertCtrl, SendCertWizardCtrl) {
    'use strict';

    var moduleName = 'ts-diabetes.controllers';

    angular.module(moduleName, []).
        controller('ts-diabetes.ViewCertCtrl', ViewCertCtrl).
        controller('ts-diabetes.SendCertWizardCtrl', SendCertWizardCtrl);

    return moduleName;
});
