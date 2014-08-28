/* global fk7263Messages */
angular.module('fk7263', [ 'ui.bootstrap', 'ngCookies', 'ngRoute', 'ngSanitize', 'common' ]);

angular.module('fk7263').config(function($routeProvider) {
    'use strict';

    $routeProvider.
        when('/fk7263/view/:certificateId', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/view-cert.html',
            controller: 'fk7263.ViewCertCtrl',
            title: 'Läkarintyg FK7263'
        }).
        when('/fk7263/recipients', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            title: 'Skicka intyg till mottagare'
        }).
        when('/fk7263/statushistory', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/status-history.html',
            controller: 'fk7263.ViewCertCtrl',
            title: 'Alla intygets händelser'
        }).
        when('/fk7263/summary', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            title: 'Kontrollera och skicka intyget'
        }).
        when('/fk7263/sent', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            title: 'Intyget har skickats'
        }).
        when('/fk7263/fel/:errorCode', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/error.html',
            controller: 'fk7263.ErrorCtrl',
            title: 'Fel'
        }).
        when('/fk7263/visafel/:errorCode', {
            templateUrl: '/web/webjars/fk7263/minaintyg/views/error.html',
            controller: 'fk7263.ErrorCtrl',
            title: 'Fel',
            backLink: '/web/start'
        });
});

// Inject language resources
angular.module('fk7263').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(fk7263Messages);
    }]);
