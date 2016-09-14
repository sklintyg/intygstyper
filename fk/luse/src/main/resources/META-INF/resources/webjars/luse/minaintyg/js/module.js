
angular.module('luse', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('luse').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('luse-view', {
            url :'/luse/view/:certificateId',
            templateUrl: '/web/webjars/luse/minaintyg/views/view-cert.html',
            controller: 'luse.ViewCertCtrl',
            data : { title: 'Läkarutlåtande för sjukersättning', keepInboxTabActive: true }
        }).
        state('luse-recipients', {
            url : '/luse/recipients',
            templateUrl: '/web/webjars/luse/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('luse-statushistory', {
            url : '/luse/statushistory',
            templateUrl: '/web/webjars/luse/minaintyg/views/status-history.html',
            controller: 'luse.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('luse-summary', {
            url : '/luse/summary',
            templateUrl: '/web/webjars/luse/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('luse-sent', {
            url : '/luse/sent',
            templateUrl: '/web/webjars/luse/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('luse-fel', {
            url : '/luse/fel/:errorCode',
            templateUrl: '/web/webjars/luse/minaintyg/views/error.html',
            controller: 'luse.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('luse-visafel', {
            url :'/luse/visafel/:errorCode',
            templateUrl: '/web/webjars/luse/minaintyg/views/error.html',
            controller: 'luse.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('luse').run(['common.messageService', 'luse.messages',
    function(messageService, luseMessages) {
        'use strict';

        messageService.addResources(luseMessages);
    }]);
