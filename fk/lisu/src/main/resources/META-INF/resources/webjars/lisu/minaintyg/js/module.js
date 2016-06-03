/* Glovbal */
angular.module('lisu', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('lisu').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('lisu-view', {
            url :'/lisu/view/:certificateId',
            templateUrl: '/web/webjars/lisu/minaintyg/views/view-cert.html',
            controller: 'lisu.ViewCertCtrl',
            data : { title: 'Läkarintyg sjukersättning', keepInboxTabActive: true }
        }).
        state('lisu-recipients', {
            url : '/lisu/recipients',
            templateUrl: '/web/webjars/lisu/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('lisu-statushistory', {
            url : '/lisu/statushistory',
            templateUrl: '/web/webjars/lisu/minaintyg/views/status-history.html',
            controller: 'lisu.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('lisu-summary', {
            url : '/lisu/summary',
            templateUrl: '/web/webjars/lisu/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('lisu-sent', {
            url : '/lisu/sent',
            templateUrl: '/web/webjars/lisu/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('lisu-fel', {
            url : '/lisu/fel/:errorCode',
            templateUrl: '/web/webjars/lisu/minaintyg/views/error.html',
            controller: 'lisu.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('lisu-visafel', {
            url :'/lisu/visafel/:errorCode',
            templateUrl: '/web/webjars/lisu/minaintyg/views/error.html',
            controller: 'lisu.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('lisu').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(lisuMessages);
    }]);
