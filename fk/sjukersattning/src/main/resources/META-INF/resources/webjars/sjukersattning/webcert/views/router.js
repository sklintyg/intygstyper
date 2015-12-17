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

                'form2@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/form2.html',
                    controller: 'sjukersattning.EditCert.Form2Ctrl'
                },
                'form4@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/form4.html',
                    controller: 'sjukersattning.EditCert.Form4Ctrl'
                },

                'form4b@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/form4b.html',
                    controller: 'sjukersattning.EditCert.Form4bCtrl'
                },

                'form5@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/form5.html',
                    controller: 'sjukersattning.EditCert.Form5Ctrl'
                },

                'form6@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/form6.html',
                    controller: 'sjukersattning.EditCert.Form6Ctrl'
                },

                'form7@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/form7.html',
                    controller: 'sjukersattning.EditCert.Form7Ctrl'
                },

                'form9@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/form9.html',
                    controller: 'sjukersattning.EditCert.Form9Ctrl'
                },

                'form10@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/form10.html',
                    controller: 'sjukersattning.EditCert.Form10Ctrl'
                },

                'form11@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/form11.html',
                    controller: 'sjukersattning.EditCert.Form11Ctrl'
                },

                'form15@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/form15.html',
                    controller: 'sjukersattning.EditCert.Form15Ctrl'
                }

            }
        });
});