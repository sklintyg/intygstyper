/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('fk7263').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('fk7263-edit', {
            url : '/fk7263/edit/:certificateId',

            //templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/intyg-edit-main.html'
            views : {
                '' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/intyg-edit-main.html',
                    controller: 'fk7263.EditCertCtrl' },
                
                'header@fk7263-edit' : {
                    templateUrl: '/web/webjars/common/webcert/intyg/edit/intyg-edit-header/intyg-edit-header.html',
                    controller: 'common.IntygEditHeader',
                    data : {
                        intygsTyp : 'fk7263'
                    }
                },
            
                'form1@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form1.html',
                    controller: 'fk7263.EditCert.Form1Ctrl'
                },

                'form4b@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form4b.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form2@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form2.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form3@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form3.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form4@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form4.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form5@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form5.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form8a@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form8a.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form8b@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form8b.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form9@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form9.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form10@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form10.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form6b@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form6b.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form6a-7-11@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form6a-7-11.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form12@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form12.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form13@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form13.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                },

                'form15@fk7263-edit' : {
                    templateUrl: '/web/webjars/fk7263/webcert/js/intyg/edit/form/intyg-edit-main-form15.html'
                    //controller: 'fk7263.EditCert.Form4bCtrl'
                }
            }
        });
});