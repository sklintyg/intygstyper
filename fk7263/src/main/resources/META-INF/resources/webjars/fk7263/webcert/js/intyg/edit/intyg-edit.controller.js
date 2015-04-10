angular.module('fk7263').controller('fk7263.EditCertCtrl',
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$stateParams', '$q',
        'common.CertificateService', 'common.ManageCertView', 'common.UserModel', 'common.wcFocus',
        'common.intygNotifyService', 'fk7263.diagnosService', 'common.DateUtilsService', 'common.UtilsService',
        'fk7263.Domain.IntygModel', 'common.Domain.DraftModel', 'fk7263.EditCertCtrl.ViewStateService',
        function($rootScope, $anchorScroll, $filter, $location, $scope, $log, $timeout, $stateParams, $q,
            CertificateService, ManageCertView, UserModel, wcFocus, intygNotifyService, diagnosService,
            dateUtils, utils, intygModel, draftModel, viewState) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/
            $scope.viewState = viewState;
            viewState.common.intyg.typ = 'fk7263';

            // Page states
            $scope.user = UserModel;

            $scope.today = new Date(); // TODO: to viewstate
            $scope.today.setHours(0, 0, 0, 0); // reset time to increase comparison accuracy (using new Date() also sets time)
            $scope.focusFirstInput = false; // TODO: to common viewstate

            // Intyg state
            $scope.cert = {};
            $scope.notifieringVidarebefordrad = draftModel.vidarebefordrad; // temporary hack. maybe move this to viewState?


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
                    vidarebefordrad: draftModel.vidarebefordrad
                };
                intygNotifyService.forwardIntyg(utkastNotifyRequest);
            };

            $scope.onVidarebefordradChange = function() {

                var utkastNotifyRequest = {
                    intygId : intygModel.id,
                    intygType: viewState.common.intyg.typ,
                    vidarebefordrad: draftModel.vidarebefordrad
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
            ManageCertView.load(viewState.common.intyg.typ, function(draftModel) {

                // check that the certs status is not signed
                if(draftModel.isSigned()){
                    // just change straight to the intyg
                    $location.url('/intyg/' + viewState.common.intyg.typ + '/' + intygModel.id);
                }

                // Decorate intygspecific default data
                $scope.cert = intygModel;

                viewState.common.isSigned = draftModel.isSigned();
                viewState.common.intyg.isComplete = draftModel.isSigned() || draftModel.isDraftComplete();
                
                $timeout(function() {
                    wcFocus('firstInput');
                    $rootScope.$broadcast('intyg.loaded', $scope.cert);
                    $rootScope.$broadcast('fk7263.loaded', intygModel);
                    viewState.common.doneLoading = true;
                }, 10);
            });

            $scope.$on('saveRequest', function($event, deferred) {

                $rootScope.$broadcast('fk7263.beforeSave', intygModel);

                // Mark form as saved, will be marked as not saved if saving fails.
                // Mark form as saved, will be marked as not saved if saving fails.
                $scope.certForm.$setPristine();
                $scope.cert.prepare();

                var intygSaveRequest = {
                    intygsId      : intygModel.id,
                    intygsTyp     : viewState.common.intyg.typ,
                    cert          : $scope.cert,
                    saveComplete  : $q.defer()
                };

                intygSaveRequest.saveComplete.promise.then(function(result) {

                    // save success
                    viewState.common.validationMessages = result.validationMessages;
                    viewState.common.validationMessagesGrouped = result.validationMessagesGrouped;
                    viewState.common.error.saveErrorMessageKey = null;

                    $rootScope.$broadcast('fk7263.afterSave', intygModel);

                }, function(result) {
                    // save failed
                    $scope.certForm.$setDirty();
                    viewState.common.error.saveErrorMessageKey = result.errorMessageKey;
                });

                deferred.resolve(intygSaveRequest);
            });

        }]);
