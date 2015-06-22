/* global fk7263Messages */
angular.module('fk7263', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common' ]);

angular.module('fk7263').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('fk7263-view', {
            url :'/fk7263/view/:certificateId',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/view-cert.html',
            controller: 'fk7263.ViewCertCtrl',
            data : { title: 'Läkarintyg FK7263' }
        }).
        state('fk7263-recipients', {
            url : '/fk7263/recipients',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('fk7263-statushistory', {
            url : '/fk7263/statushistory',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/status-history.html',
            controller: 'fk7263.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('fk7263-summary', {
            url : '/fk7263/summary',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('fk7263-sent', {
            url : '/fk7263/sent',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('fk7263-fel', {
            url : '/fk7263/fel/:errorCode',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/error.html',
            controller: 'fk7263.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('fk7263-visafel', {
            url :'/fk7263/visafel/:errorCode',
            templateUrl: '/web/webjars/fk7263/minaintyg/views/error.html',
            controller: 'fk7263.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('fk7263').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(fk7263Messages);
    }]);
