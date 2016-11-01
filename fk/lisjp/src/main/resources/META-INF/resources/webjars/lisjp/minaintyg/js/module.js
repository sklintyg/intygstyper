angular.module('lisjp', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('lisjp').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('lisjp-view', {
            url :'/lisjp/view/:certificateId',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/view-cert.html',
            controller: 'lisjp.ViewCertCtrl',
            data : { title: 'Läkarintyg för sjukpenning', keepInboxTabActive: true }
        }).
        state('lisjp-recipients', {
            url : '/lisjp/recipients',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('lisjp-statushistory', {
            url : '/lisjp/statushistory',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/status-history.html',
            controller: 'lisjp.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('lisjp-summary', {
            url : '/lisjp/summary',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('lisjp-sent', {
            url : '/lisjp/sent',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('lisjp-fel', {
            url : '/lisjp/fel/:errorCode',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/error.html',
            controller: 'lisjp.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('lisjp-visafel', {
            url :'/lisjp/visafel/:errorCode',
            templateUrl: '/web/webjars/lisjp/minaintyg/views/error.html',
            controller: 'lisjp.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('lisjp').run(['common.messageService', 'lisjp.messages',
    function(messageService, lisjpMessages) {
        'use strict';

        messageService.addResources(lisjpMessages);
    }]);
