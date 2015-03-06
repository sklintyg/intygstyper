/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-bas.router', ['ui.router']).config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('ts-bas-edit', {
            url : '/ts-bas/edit/:certificateId',
            templateUrl: '/web/webjars/ts-bas/webcert/views/edit-cert.html',
            controller: 'ts-bas.EditCertCtrl'
        });
});