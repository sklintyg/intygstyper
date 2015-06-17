/* global sjukpenningMessages */
angular.module('sjukpenning', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common' ]);

angular.module('sjukpenning').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('sjukpenning-view', {
            url :'/sjukpenning/view/:certificateId',
            templateUrl: '/web/webjars/sjukpenning/minaintyg/views/view-cert.html',
            controller: 'sjukpenning.ViewCertCtrl',
            data : { title: 'Läkarintyg' }
        }).
        state('sjukpenning-recipients', {
            url : '/sjukpenning/recipients',
            templateUrl: '/web/webjars/sjukpenning/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('sjukpenning-statushistory', {
            url : '/sjukpenning/statushistory',
            templateUrl: '/web/webjars/sjukpenning/minaintyg/views/status-history.html',
            controller: 'sjukpenning.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('sjukpenning-summary', {
            url : '/sjukpenning/summary',
            templateUrl: '/web/webjars/sjukpenning/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('sjukpenning-sent', {
            url : '/sjukpenning/sent',
            templateUrl: '/web/webjars/sjukpenning/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('sjukpenning-fel', {
            url : '/sjukpenning/fel/:errorCode',
            templateUrl: '/web/webjars/sjukpenning/minaintyg/views/error.html',
            controller: 'sjukpenning.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('sjukpenning-visafel', {
            url :'/sjukpenning/visafel/:errorCode',
            templateUrl: '/web/webjars/sjukpenning/minaintyg/views/error.html',
            controller: 'sjukpenning.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('sjukpenning').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(sjukpenningMessages);
    }]);
