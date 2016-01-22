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
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formly.html',
                    controller: 'sjukersattning.EditCert.FormlyCtrl'
                }/*,

                'formUnderlag@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formUnderlag.html',
                    controller: 'sjukersattning.EditCert.FormUnderlagCtrl'
                },
                'formSjukdomsforlopp@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formSjukdomsforlopp.html',
                    controller: 'sjukersattning.EditCert.FormSjukdomsforloppCtrl'
                },
                'formDiagnos@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formDiagnos.html',
                    controller: 'sjukersattning.EditCert.FormDiagnosCtrl'
                },

                'formDiagnos2@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formDiagnos2.html',
                    controller: 'sjukersattning.EditCert.FormDiagnos2Ctrl'
                },

                'formFunktionsnedsattning@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formFunktionsNedsattning.html',
                    controller: 'sjukersattning.EditCert.FormFunktionsNedsattningCtrl'
                },

                'formAktivitetsBegransning@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formAktivitetsBegransning.html',
                    controller: 'sjukersattning.EditCert.FormAktivitetsBegransningCtrl'
                },

                'formMedicinskaBehandlingar@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formMedicinskaBehandlingar.html',
                    controller: 'sjukersattning.EditCert.FormMedicinskaBehandlingarCtrl'
                },

                'formMedicinskaForutsattningar@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formMedicinskaForutsattningar.html',
                    controller: 'sjukersattning.EditCert.FormMedicinskaForutsattningarCtrl'
                },

                'formOvrigt@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formOvrigt.html',
                    controller: 'sjukersattning.EditCert.FormOvrigtCtrl'
                },

                'formKontakt@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formKontakt.html',
                    controller: 'sjukersattning.EditCert.FormKontaktCtrl'
                },

                'formExtraQuestions@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formExtraQuestions.html',
                    controller: 'sjukersattning.EditCert.FormExtraQuestionsCtrl'
                },

                'formGrundData@sjukersattning-edit' : {
                    templateUrl: '/web/webjars/sjukersattning/webcert/views/utkast/form/formGrundData.html',
                    controller: 'sjukersattning.EditCert.FormGrundDataCtrl'
                }
*/
            }
        });
});