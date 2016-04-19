/* Glovbal */
angular.module('luaefs', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('luaefs').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('luaefs-view', {
            url :'/luaefs/view/:certificateId',
            templateUrl: '/web/webjars/luaefs/minaintyg/views/view-cert.html',
            controller: 'luaefs.ViewCertCtrl',
            data : { title: 'Läkarintyg sjukersättning', keepInboxTabActive: true }
        }).
        state('luaefs-recipients', {
            url : '/luaefs/recipients',
            templateUrl: '/web/webjars/luaefs/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('luaefs-statushistory', {
            url : '/luaefs/statushistory',
            templateUrl: '/web/webjars/luaefs/minaintyg/views/status-history.html',
            controller: 'luaefs.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('luaefs-summary', {
            url : '/luaefs/summary',
            templateUrl: '/web/webjars/luaefs/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('luaefs-sent', {
            url : '/luaefs/sent',
            templateUrl: '/web/webjars/luaefs/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('luaefs-fel', {
            url : '/luaefs/fel/:errorCode',
            templateUrl: '/web/webjars/luaefs/minaintyg/views/error.html',
            controller: 'luaefs.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('luaefs-visafel', {
            url :'/luaefs/visafel/:errorCode',
            templateUrl: '/web/webjars/luaefs/minaintyg/views/error.html',
            controller: 'luaefs.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('luaefs').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(luaefsMessages);
    }]);
