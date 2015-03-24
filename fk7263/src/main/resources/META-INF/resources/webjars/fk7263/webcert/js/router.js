/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('fk7263').config(function($stateProvider) {
    'use strict';

    $stateProvider.
        state('fk7263-edit', {
            url : '/fk7263/edit/:certificateId?:patientId&:hospName',
            views : {
                '' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/intyg-edit.html',
                    controller: 'fk7263.EditCertCtrl' },
                
                'header@fk7263-edit' : {
                    templateUrl: '/web/webjars/common/webcert/intyg/edit/intyg-edit-header/intyg-edit-header.html',
                    controller: 'common.IntygEditHeader',
                    data : {
                        intygsTyp : 'fk7263'
                    }
                },
            
                'form1@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form1.html',
                    controller: 'fk7263.EditCert.Form1Ctrl'
                },

                'form4b@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form4b.html',
                    controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form2@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form2.html',
                    controller: 'fk7263.EditCert.Form2Ctrl'
                },

                'form3@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form3.html',
                    controller: 'fk7263.EditCert.Form3Ctrl'
                },

                'form4@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form4.html',
                    controller: 'fk7263.EditCert.Form4Ctrl'
                },

                'form5@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form5.html',
                    controller: 'fk7263.EditCert.Form5Ctrl'
                },

                'form8a@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form8a.html',
                    controller: 'fk7263.EditCert.Form8aCtrl'
                },

                'form8b@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form8b.html',
                    controller: 'fk7263.EditCert.Form8bCtrl'
                },

                'form9@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form9.html',
                    controller: 'fk7263.EditCert.Form9Ctrl'
                },

                'form10@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form10.html',
                    controller: 'fk7263.EditCert.Form10Ctrl'
                },

                'form6b@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form6b.html',
                    controller: 'fk7263.EditCert.Form6bCtrl'
                },

                'form6a-7-11@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form6a-7-11.html',
                    controller: 'fk7263.EditCert.Form6a711Ctrl'
                },

                'form12@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form12.html',
                    controller: 'fk7263.EditCert.Form12Ctrl'
                },

                'form13@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form13.html',
                    controller: 'fk7263.EditCert.Form13Ctrl'
                },

                'form15@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/form15.html',
                    controller: 'fk7263.EditCert.Form15Ctrl'
                }
            }
        });
});