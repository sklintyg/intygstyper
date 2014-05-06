define([ 'angular', 'services', 'ts-diabetes/intyg/js/controllers', 'ts-diabetes/intyg/js/messages', 'ts-diabetes/intyg/js/services',
        'ts-diabetes/intyg/js/filters', ], function(angular, miServices, controllers, messages, services, filters) {
    'use strict';

    var moduleName = 'ts-diabetes';

    var module = angular.module(moduleName, [ miServices, controllers, services, filters ]);

    module.config([ '$routeProvider', function($routeProvider) {
        $routeProvider.when('/ts-diabetes/view/:certificateId', {
            templateUrl: '/web/webjars/ts-diabetes/intyg/views/view-cert.html',
            controller: 'ts-diabetes.ViewCertCtrl',
            title: "Läkarintyg Transportstyrelsen diabetes"
        }).when('/ts-diabetes/recipients', {
            templateUrl: '/web/webjars/ts-diabetes/intyg/views/recipients.html',
            controller: 'ts-diabetes.SendCertWizardCtrl'
        }).when('/ts-diabetes/summary', {
            templateUrl: '/web/webjars/ts-diabetes/intyg/views/send-summary.html',
            controller: 'ts-diabetes.SendCertWizardCtrl',
            title: "Kontrollera och skicka intyget"
        }).when('/ts-diabetes/sent', {
            templateUrl: '/web/webjars/ts-diabetes/intyg/views/sent-cert.html',
            controller: 'ts-diabetes.SendCertWizardCtrl',
            title: "Intyget skickat till mottagare"
        });
    } ]);
    // Inject language resources
    // TODO: This only works since we always load webcert before the module, when the messageService
    // is moved to a commons project, make sure this is loaded for this module as well.
    module.run([ 'messageService', function(messageService) {
        messageService.addResources(messages);
    } ]);

    return moduleName;
});
