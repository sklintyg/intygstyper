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
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/edit-cert.html',
                    controller: 'ts-diabetes.EditCertCtrl'
                },
                'header@ts-diabetes-edit': {
                    templateUrl: '/web/webjars/common/webcert/intyg/edit/intyg-edit-header/intyg-edit-header.html',
                    controller: 'common.IntygEditHeader',
                    data: {
                        intygsTyp: 'ts-diabetes'
                    }
                }
            }
        });
});

