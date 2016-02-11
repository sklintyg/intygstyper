/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('lisu').config(function($stateProvider) {
    'use strict';
// TODO: clean up unused routes!
    $stateProvider.
        state('lisu-edit', {
            data: { defaultActive : 'index', intygType: 'lisu' },
            url : '/lisu/edit/:certificateId?:patientId&:hospName',
            views : {
                'content@' : {
                    templateUrl: '/web/webjars/sjukpenning-utokad/webcert/views/utkast/utkast.html',
                    controller: 'sjukpenning-utokad.EditCertCtrl'
                },

                'wcHeader@lisu-edit' : {
                    templateUrl: '/web/webjars/common/webcert/gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },

                'header@lisu-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@lisu-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'formly@lisu-edit' : {
                    templateUrl: '/web/webjars/sjukpenning-utokad/webcert/views/utkast/sjukpenning-utokad.formly.html',
                    controller: 'sjukpenning-utokad.EditCert.FormlyCtrl'
                }
            }
        });
});