/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('luse').config(function($stateProvider) {
    'use strict';
// TODO: clean up unused routes!
    $stateProvider.
        state('luse-edit', {
            data: { defaultActive : 'index', intygType: 'luse' },
            url : '/luse/edit/:certificateId?:patientId&:hospName',
            views : {
                'content@' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/utkast.html',
                    controller: 'sjukersattning.EditCertCtrl'
                },

                'wcHeader@luse-edit' : {
                    templateUrl: '/web/webjars/common/webcert/gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },

                'header@luse-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@luse-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@luse-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/sjukersattning.formly.html',
                    controller: 'sjukersattning.EditCert.FormlyCtrl'
                }
            }
        });
});