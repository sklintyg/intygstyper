/* global sjukersattningMessages */
angular.module('sjukersattning', [ 'ui.bootstrap', 'ngCookies', 'ui.router', 'ngSanitize', 'common' ]);

angular.module('sjukersattning').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('sjukersattning-view', {
            url :'/sjukersattning/view/:certificateId',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/view-cert.html',
            controller: 'sjukersattning.ViewCertCtrl',
            data : { title: 'Läkarintyg' }
        }).
        state('sjukersattning-recipients', {
            url : '/sjukersattning/recipients',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Skicka intyg till mottagare' }
        }).
        state('sjukersattning-statushistory', {
            url : '/sjukersattning/statushistory',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/status-history.html',
            controller: 'sjukersattning.ViewCertCtrl',
            data : { title: 'Alla intygets händelser' }
        }).
        state('sjukersattning-summary', {
            url : '/sjukersattning/summary',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Kontrollera och skicka intyget' }
        }).
        state('sjukersattning-sent', {
            url : '/sjukersattning/sent',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data : { title: 'Intyget har skickats' }
        }).
        state('sjukersattning-fel', {
            url : '/sjukersattning/fel/:errorCode',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/error.html',
            controller: 'sjukersattning.ErrorCtrl',
            data : { title: 'Fel' }
        }).
        state('sjukersattning-visafel', {
            url :'/sjukersattning/visafel/:errorCode',
            templateUrl: '/web/webjars/sjukersattning/minaintyg/views/error.html',
            controller: 'sjukersattning.ErrorCtrl',
            data : { title: 'Fel',
                    backLink: '/web/start' }
        });
});

// Inject language resources
angular.module('sjukersattning').run(['common.messageService',
    function(messageService) {
        'use strict';

        messageService.addResources(sjukersattningMessages);
    }]);
