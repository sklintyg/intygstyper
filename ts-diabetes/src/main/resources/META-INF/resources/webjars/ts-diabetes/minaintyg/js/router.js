/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-diabetes').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('ts-diabetes-view', {
            url :'/ts-diabetes/view/:certificateId',
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/view-cert.html',
            controller: 'ts-diabetes.ViewCertCtrl',
            data:{title: 'Läkarintyg Transportstyrelsen diabetes', keepInboxTabActive: true}
        }).
        state('ts-diabetes-recipients', {
            url :'/ts-diabetes/recipients',
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/recipients.html',
            controller: 'common.SendCertWizardCtrl',
            data: { title: 'Skicka intyg till mottagare' }
        }).
        state('ts-diabetes-summary', {
            url : '/ts-diabetes/summary',
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/send-summary.html',
            controller: 'common.SendCertWizardCtrl',
            data:{title: 'Kontrollera och skicka intyget'}
        }).
        state('ts-diabetes-sent', {
            url : '/ts-diabetes/sent',
            templateUrl: '/web/webjars/ts-diabetes/minaintyg/views/sent-cert.html',
            controller: 'common.SendCertWizardCtrl',
            data:{title: 'Intyget skickat till mottagare'}
        });
});