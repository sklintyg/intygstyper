/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('sjukpenning').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('sjukpenning-edit', {
            data: { defaultActive : 'index' },
            url : '/sjukpenning/edit/:certificateId?:patientId&:hospName',
            views : {
                'content@' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/utkast.html',
                    controller: 'sjukpenning.EditCertCtrl'
                },

                'wcHeader@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/common/webcert/gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },

                'header@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'footer@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                },

                'form1@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form1.html',
                    controller: 'sjukpenning.EditCert.Form1Ctrl'
                },

                'form4b@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form4b.html',
                    controller: 'sjukpenning.EditCert.Form4bCtrl'
                },

                'form2@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form2.html',
                    controller: 'sjukpenning.EditCert.Form2Ctrl'
                },

                'form3@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form3.html',
                    controller: 'sjukpenning.EditCert.Form3Ctrl'
                },

                'form4@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form4.html',
                    controller: 'sjukpenning.EditCert.Form4Ctrl'
                },

                'form5@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form5.html',
                    controller: 'sjukpenning.EditCert.Form5Ctrl'
                },

                'form8a@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form8a.html',
                    controller: 'sjukpenning.EditCert.Form8aCtrl'
                },

                'form8b@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form8b.html',
                    controller: 'sjukpenning.EditCert.Form8bCtrl'
                },

                'form9@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form9.html',
                    controller: 'sjukpenning.EditCert.Form9Ctrl'
                },

                'form10@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form10.html',
                    controller: 'sjukpenning.EditCert.Form10Ctrl'
                },

                'form6b@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form6b.html',
                    controller: 'sjukpenning.EditCert.Form6bCtrl'
                },

                'form6a-7-11@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form6a-7-11.html',
                    controller: 'sjukpenning.EditCert.Form6a711Ctrl'
                },

                'form12@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form12.html',
                    controller: 'sjukpenning.EditCert.Form12Ctrl'
                },

                'form13@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form13.html',
                    controller: 'sjukpenning.EditCert.Form13Ctrl'
                },

                'form15@sjukpenning-edit' : {
                    templateUrl: '/web/webjars/sjukpenning/webcert/views/utkast/form/form15.html',
                    controller: 'sjukpenning.EditCert.Form15Ctrl'
                }
            }
        });
});