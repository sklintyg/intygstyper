/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-diabetes.router', ['ui.router']).config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('ts-diabetes-edit', {
            url: '/ts-diabetes/edit/:certificateId',
            templateUrl: '/web/webjars/ts-diabetes/webcert/views/edit-cert.html',
            controller: 'ts-diabetes.EditCertCtrl'
        });
});

