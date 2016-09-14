
angular.module('luae_na', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('luae_na').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('luae_na-view', {
            url :'/luae_na/view/:certificateId',
            templateUrl: '/web/webjars/luae_na/minaintyg/views/view-cert.html',
            controller: 'luae_na.ViewCertCtrl',
            data : { title: 'Läkarintyg aktivitetsersättning nedsatt arbetsförmåga', keepInboxTabActive: true }
        }).
        state('luae_na-recipients', {
            url : '/luae_na/recipients',
            templateUrl: '/web/webjars/luae_na/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('luae_na-statushistory', {
            url : '/luae_na/statushistory',
            templateUrl: '/web/webjars/luae_na/minaintyg/views/status-history.html',
            controller: 'luae_na.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('luae_na-summary', {
            url : '/luae_na/summary',
            templateUrl: '/web/webjars/luae_na/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('luae_na-sent', {
            url : '/luae_na/sent',
            templateUrl: '/web/webjars/luae_na/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('luae_na-fel', {
            url : '/luae_na/fel/:errorCode',
            templateUrl: '/web/webjars/luae_na/minaintyg/views/error.html',
            controller: 'luae_na.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('luae_na-visafel', {
            url :'/luae_na/visafel/:errorCode',
            templateUrl: '/web/webjars/luae_na/minaintyg/views/error.html',
            controller: 'luae_na.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('luae_na').run(['common.messageService', 'luae_na.messages',
    function(messageService, luaeNaMessages) {
        'use strict';

        messageService.addResources(luaeNaMessages);
    }]);
