/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('sjukersattning').config(function($stateProvider) {
    'use strict';
// TODO: clean up unused routes!
    $stateProvider.
        state('sjukersattning-edit', {
            data: { defaultActive : 'index' },
            url : '/sjukersattning/edit/:certificateId?:patientId&:hospName',
            views : {
                'content@' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/utkast.html',
                    controller: 'sjukersattning.EditCertCtrl'
                },

                'wcHeader@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/common/webcert/gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },

                'header@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/sjukersattning.formly.html',
                    controller: 'sjukersattning.EditCert.FormlyCtrl'
                }
            }
        });
});