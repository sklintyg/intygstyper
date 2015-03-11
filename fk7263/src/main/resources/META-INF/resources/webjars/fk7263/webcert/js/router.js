/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('fk7263').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('fk7263-edit', {
            url: '/fk7263/edit/:certificateId?patientId&hospName',
            views: {
                '' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/intyg-edit.html',
                    controller: 'fk7263.EditCertCtrl'
                },
                'header@fk7263-edit' : {
                    templateUrl: '/web/webjars/common/webcert/intyg/edit/intyg-edit-header/intyg-edit-header.html',
                    controller: 'common.IntygEditHeader',
                    data : {
                        intygsTyp : 'fk7263'
                    }
                }
            }
        });
});