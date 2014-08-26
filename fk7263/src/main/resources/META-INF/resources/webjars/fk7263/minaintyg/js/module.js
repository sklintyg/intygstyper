define([
    'angular',
    'services',
    'fk7263/minaintyg/js/controllers',
    'fk7263/minaintyg/js/messages',
    'fk7263/minaintyg/js/services',
    'common/minaintyg/js/wc-common-message-resources'
], function(angular, miServices, controllers, messages, services, commonMessages) {
    'use strict';

    var moduleName = 'fk7263';

    var module = angular.module(moduleName, [miServices, controllers, services]);

    module.config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/fk7263/view/:certificateId', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/view-cert.html',
            controller: 'fk7263.ViewCertCtrl',
            title: 'Läkarintyg FK7263'
        }).when('/fk7263/recipients', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/recipients.html',
            controller: 'fk7263.SendCertWizardCtrl',
            title: 'Skicka intyg till mottagare'
        }).when('/fk7263/statushistory', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/status-history.html',
            controller: 'fk7263.ViewCertCtrl',
            title: 'Alla intygets händelser'
        }).when('/fk7263/summary', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/send-summary.html',
            controller: 'fk7263.SendCertWizardCtrl',
            title: 'Kontrollera och skicka intyget'
        }).when('/fk7263/sent', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/sent-cert.html',
            controller: 'fk7263.SendCertWizardCtrl',
            title: 'Intyget har skickats'
        }).when('/fk7263/fel/:errorCode', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/error.html',
            controller: 'fk7263.ErrorCtrl',
            title: 'Fel'
        }).when('/fk7263/visafel/:errorCode', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/error.html',
            controller: 'fk7263.ErrorCtrl',
            title: 'Fel',
            backLink: '/web/start'
        });
    } ]);

    // Inject language resources
    // TODO: This only works since we always load webcert before the module, when the messageService
    // is moved to a commons project, make sure this is loaded for this module as well.
    module.run(['messageService',
        function(messageService) {
            messageService.addResources(messages);
            messageService.addResources(commonMessages);
        }
    ]);

    return moduleName;
});
