define([
    'angular',
    'services',
    'ts-diabetes/minaintyg/js/controllers',
    'ts-diabetes/minaintyg/js/messages'
], function(angular, miServices, controllers, messages) {
    'use strict';

    var moduleName = 'ts-diabetes';

    var module = angular.module(moduleName, [ miServices, controllers ]);

    module.config([ '$routeProvider', function($routeProvider) {
        $routeProvider.when('/ts-diabetes/view/:certificateId', {
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/view-cert.html',
            controller: 'ts-diabetes.ViewCertCtrl',
            title: 'Läkarintyg Transportstyrelsen diabetes'
        }).when('/ts-diabetes/recipients', {
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/recipients.html',
            controller: 'ts-diabetes.SendCertWizardCtrl'
        }).when('/ts-diabetes/summary', {
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/send-summary.html',
            controller: 'ts-diabetes.SendCertWizardCtrl',
            title: 'Kontrollera och skicka intyget'
        }).when('/ts-diabetes/sent', {
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/sent-cert.html',
            controller: 'ts-diabetes.SendCertWizardCtrl',
            title: 'Intyget skickat till mottagare'
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
