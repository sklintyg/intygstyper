define([
    'angular',
    'fk7263/minaintyg/js/services',
    'common/minaintyg/js/controllers/SendCertWizardCtrl',
    'fk7263/minaintyg/js/controllers/ErrorCtrl',
    'fk7263/minaintyg/js/controllers/ViewCertCtrl'
], function(angular, services, SendCertWizardCtrl, ErrorCtrl, ViewCertCtrl) {
    'use strict';

    var moduleName = 'fk7263.controllers';

    angular.module(moduleName, [services]).
        controller('fk7263.SendCertWizardCtrl', SendCertWizardCtrl).
        controller('fk7263.ErrorCtrl', ErrorCtrl).
        controller('fk7263.ViewCertCtrl', ViewCertCtrl);

    return moduleName;
});
