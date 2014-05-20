define(['angular',
        'ts-diabetes/intyg/js/services',
        'ts-diabetes/intyg/js/controllers/ViewCertCtrl',
        'ts-diabetes/intyg/js/controllers/SendCertWizardCtrl' ],
    function(angular, services, ViewCertCtrl, SendCertWizardCtrl) {
    'use strict';

    var moduleName = 'ts-diabetes.controllers';

    angular.module(moduleName, [ services ]).
        controller('ts-diabetes.ViewCertCtrl', ViewCertCtrl).
        controller('ts-diabetes.SendCertWizardCtrl', SendCertWizardCtrl);

    return moduleName;
});
