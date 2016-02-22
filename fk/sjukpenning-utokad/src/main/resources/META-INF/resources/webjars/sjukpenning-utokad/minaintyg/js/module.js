/* Glovbal */
angular.module('lisu', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('lisu').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('sjukpenning-utokad-view', {
            url :'/lisu/view/:certificateId',
            templateUrl: '/web/webjars/sjukpenning-utokad/minaintyg/views/view-cert.html',
            controller: 'sjukpenning-utokad.ViewCertCtrl',
            data : { title: 'Läkarintyg sjukersättning', keepInboxTabActive: true }
        }).
        state('sjukpenning-utokad-recipients', {
            url : '/lisu/recipients',
            templateUrl: '/web/webjars/sjukpenning-utokad/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('sjukpenning-utokad-statushistory', {
            url : '/lisu/statushistory',
            templateUrl: '/web/webjars/sjukpenning-utokad/minaintyg/views/status-history.html',
            controller: 'sjukpenning-utokad.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('sjukpenning-utokad-summary', {
            url : '/lisu/summary',
            templateUrl: '/web/webjars/sjukpenning-utokad/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('sjukpenning-utokad-sent', {
            url : '/lisu/sent',
            templateUrl: '/web/webjars/sjukpenning-utokad/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('sjukpenning-utokad-fel', {
            url : '/lisu/fel/:errorCode',
            templateUrl: '/web/webjars/sjukpenning-utokad/minaintyg/views/error.html',
            controller: 'sjukpenning-utokad.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('sjukpenning-utokad-visafel', {
            url :'/lisu/visafel/:errorCode',
            templateUrl: '/web/webjars/sjukpenning-utokad/minaintyg/views/error.html',
            controller: 'sjukpenning-utokad.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('lisu').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(sjukpenningUtokadMessages);
    }]);
