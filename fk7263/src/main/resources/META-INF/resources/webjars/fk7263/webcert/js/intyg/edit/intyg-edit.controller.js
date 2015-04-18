angular.module('fk7263').controller('fk7263.EditCertCtrl',
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$stateParams', '$q',
        'common.CertificateService', 'common.ManageCertView', 'common.UserModel',
        'common.intygNotifyService', 'fk7263.diagnosService', 'common.DateUtilsService', 'common.UtilsService',
        'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService',
        function($rootScope, $anchorScroll, $filter, $location, $scope, $log, $timeout, $stateParams, $q,
            CertificateService, ManageCertView, UserModel, intygNotifyService, diagnosService,
            dateUtils, utils, IntygModel, viewState) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/
            // create a new intyg model
            viewState.setDraftModel(IntygModel._members.build());
            $scope.viewState = viewState;
            viewState.common.intyg.typ = 'fk7263';

            // Page states
            $scope.user = UserModel;

            $scope.today = new Date(); // TODO: to viewstate
            $scope.today.setHours(0, 0, 0, 0); // reset time to increase comparison accuracy (using new Date() also sets time)
            $scope.focusFirstInput = false; // TODO: to common viewstate

            // Intyg state
            $scope.cert = viewState.intygModel;
            $scope.notifieringVidarebefordrad = viewState.draftModel.vidarebefordrad; // temporary hack. maybe move this to viewState?


            // TODO : see if the below can be removed
            // Keeps track of in-form interactions which is converted to internal model on save,
            // and converted from internal model on load
            $scope.form = {
                arbete: true,
                prognos: 'YES',
                rehab: 'NEJ',
                ressattTillArbeteAktuellt: undefined,
                ovrigt: {
                    'annanReferensBeskrivning': null,
                    'nedsattMed25Beskrivning': null,
                    'nedsattMed50Beskrivning': null,
                    'nedsattMed75Beskrivning': null,
                    'arbetsformagaPrognosGarInteAttBedomaBeskrivning': null
                }
            };

            $scope.stash = {
                cert : { kommentar : ''}
            };


            /****************************************************************************
             * Exposed interaction functions to view
             ****************************************************************************/

            /**
             * Handle vidarebefordra dialog
             */
            $scope.openMailDialog = function() {

                var utkastNotifyRequest = {
                    intygId : intygModel.id,
                    intygType: viewState.common.intyg.typ,
                    vidarebefordrad: viewState.draftModel.vidarebefordrad
                };
                intygNotifyService.forwardIntyg(utkastNotifyRequest);
            };

            $scope.onVidarebefordradChange = function() {

                var utkastNotifyRequest = {
                    intygId : intygModel.id,
                    intygType: viewState.common.intyg.typ,
                    vidarebefordrad: viewState.draftModel.vidarebefordrad
                };
                intygNotifyService.onForwardedChange(utkastNotifyRequest);
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                ManageCertView.signera(viewState.common.intyg.typ);
            };

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/

            // Get the certificate draft from the server.
            ManageCertView.load(viewState.common.intyg.typ, viewState.draftModel);

            $scope.$on('saveRequest', function($event, deferred) {

                $rootScope.$broadcast('fk7263.beforeSave', viewState.intygModel);

                // Mark form as saved, will be marked as not saved if saving fails.
                $scope.certForm.$setPristine();

                var intygSaveRequest = {
                    intygsId      : viewState.intygModel.id,
                    intygsTyp     : viewState.common.intyg.typ,
                    cert          : viewState.intygModel.toSendModel(),
                    saveComplete  : $q.defer()
                };

                intygSaveRequest.saveComplete.promise.then(function(result) {

                    // save success
                    viewState.common.validationMessages = result.validationMessages;
                    viewState.common.validationMessagesGrouped = result.validationMessagesGrouped;
                    viewState.common.error.saveErrorMessageKey = null;

                    $rootScope.$broadcast('fk7263.afterSave', viewState.intygModel);

                }, function(result) {
                    // save failed
                    $scope.certForm.$setDirty();
                    viewState.common.error.saveErrorMessageKey = result.errorMessageKey;
                });

                deferred.resolve(intygSaveRequest);
            });


        }]);
