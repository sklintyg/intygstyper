/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-diabetes').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('ts-diabetes-edit', {
            url: '/ts-diabetes/edit/:certificateId',
            views : {
                '': {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/js/utkast/utkast.html',
                    controller: 'ts-diabetes.UtkastController'
                },
                'header@ts-diabetes-edit': {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-header/utkast-header.html',
                    controller: 'common.UtkastHeader'
                }
            }
        });
});

