
angular.module('luae_fs', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common']);

angular.module('luae_fs').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('luae_fs-view', {
            url :'/luae_fs/view/:certificateId',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/view-cert.html',
            controller: 'luae_fs.ViewCertCtrl',
            data : { title: 'Läkarutlåtande för aktivitetsersättning vid förlängd skolgång', keepInboxTabActive: true }
        }).
        state('luae_fs-recipients', {
            url : '/luae_fs/recipients',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('luae_fs-statushistory', {
            url : '/luae_fs/statushistory',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/status-history.html',
            controller: 'luae_fs.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('luae_fs-summary', {
            url : '/luae_fs/summary',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('luae_fs-sent', {
            url : '/luae_fs/sent',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('luae_fs-fel', {
            url : '/luae_fs/fel/:errorCode',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/error.html',
            controller: 'luae_fs.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('luae_fs-visafel', {
            url :'/luae_fs/visafel/:errorCode',
            templateUrl: '/web/webjars/luae_fs/minaintyg/views/error.html',
            controller: 'luae_fs.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('luae_fs').run(['common.messageService', 'luae_fs.messages',
    function(messageService, luaeFsMessages) {
        'use strict';

        messageService.addResources(luaeFsMessages);
    }]);
