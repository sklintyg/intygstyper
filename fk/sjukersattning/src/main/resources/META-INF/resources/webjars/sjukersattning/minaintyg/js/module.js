/* Glovbal */
angular.module('luse', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('luse').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('sjukersattning-view', {
            url :'/luse/view/:certificateId',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/view-cert.html',
            controller: 'luse.ViewCertCtrl',
            data : { title: 'Läkarintyg sjukersättning', keepInboxTabActive: true }
        }).
        state('sjukersattning-recipients', {
            url : '/luse/recipients',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('sjukersattning-statushistory', {
            url : '/luse/statushistory',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/status-history.html',
            controller: 'luse.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('sjukersattning-summary', {
            url : '/luse/summary',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('sjukersattning-sent', {
            url : '/luse/sent',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('sjukersattning-fel', {
            url : '/luse/fel/:errorCode',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/error.html',
            controller: 'sjukersattning.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('sjukersattning-visafel', {
            url :'/luse/visafel/:errorCode',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/error.html',
            controller: 'sjukersattning.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('luse').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(sjukersattningMessages);
    }]);
