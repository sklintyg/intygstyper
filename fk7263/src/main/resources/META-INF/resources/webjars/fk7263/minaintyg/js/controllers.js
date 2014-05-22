define([
    'angular',
    'fk7263/minaintyg/js/services',
    'fk7263/minaintyg/js/controllers/SentCertWizardCtrl',
    'fk7263/minaintyg/js/controllers/ErrorCtrl',
    'fk7263/minaintyg/js/controllers/ViewCertCtrl'
], function(angular, services, SentCertWizardCtrl, ErrorCtrl, ViewCertCtrl) {
    'use strict';

    var moduleName = 'fk7263.controllers';

    angular.module(moduleName, [services]).
        controller('fk7263.SentCertWizardCtrl', SentCertWizardCtrl).
        controller('fk7263.ErrorCtrl', ErrorCtrl).
        controller('fk7263.ViewCertCtrl', ViewCertCtrl);

    return moduleName;
});
