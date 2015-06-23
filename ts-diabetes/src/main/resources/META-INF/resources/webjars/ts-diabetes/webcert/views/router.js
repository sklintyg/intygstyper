/**
 * Created by stephenwhite on 05/03/15.
 */
angular.module('ts-diabetes').config(function($stateProvider) {
    'use strict';
    $stateProvider.
        state('ts-diabetes-edit', {
            data: { defaultActive : 'index' },
            url: '/ts-diabetes/edit/:certificateId',
            views : {
                'content@': {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/utkast.html',
                    controller: 'ts-diabetes.UtkastController'
                },
                'wcHeader@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/common/webcert/gui/headers/wcHeader.partial.html',
                    controller: 'common.wcHeaderController'
                },
                'header@ts-diabetes-edit': {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-header/utkastHeader.html',
                    controller: 'common.UtkastHeader'
                },

                'patient@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/patient.html'
                },
                'intention@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/intention.html',
                    controller: 'ts-diabetes.Utkast.IntentionController'
                },
                'identity@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/identity.html'
                },
                'alert@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/alert.html'
                },

                'form1@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/form1.html',
                    controller: 'ts-diabetes.Utkast.Form1Controller'
                },
                'form2@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/form2.html',
                    controller: 'ts-diabetes.Utkast.Form2Controller'
                },
                'form3@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/form3.html',
                    controller: 'ts-diabetes.Utkast.Form3Controller'
                },
                'form4@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/form4.html',
                    controller: 'ts-diabetes.Utkast.Form4Controller'
                },
                'form5@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/form5.html'
                },
                'form6@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/form6.html'
                },
                'form7@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/form7.html'
                },

                'messages@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/ts-diabetes/webcert/views/utkast/form/messages.html',
                    controller: 'ts-diabetes.Utkast.MessagesController'
                },

                'footer@ts-diabetes-edit' : {
                    templateUrl: '/web/webjars/common/webcert/utkast/utkast-footer/utkastFooter.html',
                    controller: 'common.UtkastFooter'
                }
            }
        });
});
