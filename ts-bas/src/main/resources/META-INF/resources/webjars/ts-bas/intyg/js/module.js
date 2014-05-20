define([
    'angular',
    'services',
    'ts-bas/intyg/js/controllers',
    'ts-bas/intyg/js/messages',
    'common/js/minaintyg/CertificateService',
    'ts-bas/intyg/js/filters'
], function(angular, miServices, controllers, messages, miCertificateService, filters) {
    'use strict';

    var moduleName = 'ts-bas';

    var module = angular.module(moduleName, [miServices, controllers, miCertificateService, filters]);

    module.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/ts-bas/view/:certificateId', {
                templateUrl: '/web/webjars/ts-bas/intyg/views/view-cert.html',
                controller: 'ts-bas.ViewCertCtrl',
                title: 'Läkarintyg Transportstyrelsen Bas'
            }).
            when('/ts-bas/recipients', {
                templateUrl: '/web/webjars/ts-bas/intyg/views/recipients.html',
                controller: 'ts-bas.SendCertWizardCtrl'
            }).
            when('/ts-bas/summary', {
                templateUrl: '/web/webjars/ts-bas/intyg/views/send-summary.html',
                controller: 'ts-bas.SendCertWizardCtrl',
                title: 'Kontrollera och skicka intyget'
            }).
            when('/ts-bas/sent', {
                templateUrl: '/web/webjars/ts-bas/intyg/views/sent-cert.html',
                controller: 'ts-bas.SendCertWizardCtrl',
                title: 'Intyget skickat till mottagare'
            });
    }]);
    // Inject language resources
    // TODO: This only works since we always load webcert before the module, when the messageService
    // is moved to a commons project, make sure this is loaded for this module as well.
    module.run(['messageService',
        function(messageService) {
            messageService.addResources(messages);
        }
    ]);

    return moduleName;
});
