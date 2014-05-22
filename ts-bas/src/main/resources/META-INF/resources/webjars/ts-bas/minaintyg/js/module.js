define([
    'angular',
    'services',
    'ts-bas/minaintyg/js/controllers',
    'ts-bas/minaintyg/js/messages'
], function(angular, miServices, controllers, messages ) {
    'use strict';

    var moduleName = 'ts-bas';

    var module = angular
        .module(moduleName, [miServices, controllers ]);

    module.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
            when('/ts-bas/view/:certificateId', {
                templateUrl: '/web/webjars/ts-bas/minaintyg/views/view-cert.html',
                controller: 'ts-bas.ViewCertCtrl',
                title: 'Läkarintyg Transportstyrelsen Bas'
            }).
            when('/ts-bas/recipients', {
                templateUrl: '/web/webjars/ts-bas/minaintyg/views/recipients.html',
                controller: 'ts-bas.SendCertWizardCtrl'
            }).
            when('/ts-bas/summary', {
                templateUrl: '/web/webjars/ts-bas/minaintyg/views/send-summary.html',
                controller: 'ts-bas.SendCertWizardCtrl',
                title: 'Kontrollera och skicka intyget'
            }).
            when('/ts-bas/sent', {
                templateUrl: '/web/webjars/ts-bas/minaintyg/views/sent-cert.html',
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
