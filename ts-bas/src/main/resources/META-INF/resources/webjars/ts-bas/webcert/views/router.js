/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-bas').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('ts-bas-edit', {
            url : '/ts-bas/edit/:certificateId',
            views : {
                '': {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/utkast.html',
                    controller: 'ts-bas.UtkastController' },

                'header@ts-bas-edit': {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'patient@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/patient.html'
                },
                'intention@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/intention.html'
                },
                'identity@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/identity.html'
                },
                'alert@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/alert.html'
                },

                'form1@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form1.html'
                },
                'form2@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form2.html'
                },
                'form3@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form3.html'
                },
                'form4@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form4.html'
                },
                'form5@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form5.html'
                },
                'form6@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form6.html'
                },
                'form7@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form7.html'
                },
                'form8@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form8.html'
                },
                'form9@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form9.html'
                },
                'form10@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form10.html'
                },
                'form11@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form11.html'
                },
                'form12@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form12.html'
                },
                'form13@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form13.html'
                },
                'form14@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form14.html'
                },
                'form15@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form15.html'
                },
                'form16@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form16.html'
                },
                'form17@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form17.html'
                },
                'form18@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form18.html'
                },
                'form19@ts-bas-edit' : {
                    templateUrl: '/web/webjars/ts-bas/webcert/views/utkast/form/form19.html'
                },

                'footer@ts-bas-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                }

            }
        });
});