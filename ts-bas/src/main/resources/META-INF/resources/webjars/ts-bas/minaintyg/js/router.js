/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-bas').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('ts-bas-view', {
            url :'/ts-bas/view/:certificateId',
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/view-cert.html',
            controller: 'ts-bas.ViewCertCtrl',
            data:{title: 'Läkarintyg Transportstyrelsen Bas'}
        }).
        state('ts-bas-recipients', {
            url : '/ts-bas/recipients',
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl'
        }).
        state('ts-bas-summary', {
            url :'/ts-bas/summary',
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data:{title: 'Kontrollera och skicka intyget'}
        }).
        state('ts-bas-sent', {
            url :'/ts-bas/sent',
            templateUrl: '/web/webjars/ts-bas/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data:{title: 'Intyget skickat till mottagare'}
        });
});
