angular.module('fk7263').controller('fk7263.EditCertCtrl',
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$stateParams', '$q',
        'common.CertViewState', 'common.CertificateService', 'common.ManageCertView', 'common.User', 'common.wcFocus',
        'common.intygNotifyService', 'fk7263.diagnosService', 'common.DateUtilsService', 'common.UtilsService',
        'fk7263.Domain.IntygModel','fk7263.EditCertCtrl.ViewStateService',
        'fk7263.EditCertCtrl.Helper',
        function($rootScope, $anchorScroll, $filter, $location, $scope, $log, $timeout, $stateParams, $q, CertViewState,
            CertificateService, ManageCertView, User, wcFocus, intygNotifyService, diagnosService, dateUtils, utils, intygModel,
            viewState, helper) {
            'use strict';

            /**************************************************************************
             * Private vars
             */


            /**********************************************************************************
             * Default state
             **********************************************************************************/
            $scope.viewState = viewState;

            // Page states
            $scope.user = User;
            $scope.today = new Date();
            $scope.today.setHours(0, 0, 0, 0); // reset time to increase comparison accuracy (using new Date() also sets time)
            $scope.focusFirstInput = false;
            $scope.viewState = CertViewState.viewState;

            // Intyg state
            $scope.cert = {};
            $scope.hasSavedThisSession = false;
            $scope.messages = [];
            $scope.isComplete = false;
            $scope.isSigned = false;

            $scope.certMeta = {
                intygId: null,
                intygType: 'fk7263',
                vidarebefordrad: false
            };


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
            }


            /****************************************************************************
             * Exposed interaction functions to view
             ****************************************************************************/

            /**
             * Handle vidarebefordra dialog
             */
            $scope.openMailDialog = function() {
                intygNotifyService.forwardIntyg($scope.certMeta, CertViewState.viewState);
            };

            $scope.onVidarebefordradChange = function() {
                intygNotifyService.onForwardedChange($scope.certMeta, CertViewState.viewState);
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                ManageCertView.signera($scope.certMeta.intygType);
            };

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/

                // Get the certificate draft from the server.
            ManageCertView.load( $scope.certMeta.intygType, function(draftModel) {

                //intygModel.update(cert);

                // check that the certs status is not signed
                if($scope.isSigned){
                    // just change straight to the intyg
                    $location.url('/intyg/' + $scope.certMeta.intygType + '/' + $scope.certMeta.intygId);
                }

                // Decorate intygspecific default data
                $scope.cert = intygModel;
                //$scope.cert = data.content;
                $scope.certMeta.intygId = intygModel.id;
                $scope.certMeta.vidarebefordrad = draftModel.vidarebefordrad;
                CertViewState.viewState.isSigned = draftModel.status === 'SIGNED';
                CertViewState.viewState.intyg.isComplete = $scope.isSigned || draftModel.status === 'DRAFT_COMPLETE';

                $timeout(function() {
                    wcFocus('firstInput');
                    $rootScope.$broadcast('intyg.loaded', $scope.cert);
                    CertViewState.viewState.doneLoading = true;
                }, 10);
            });

            $rootScope.$on('intyg.deleted', function() {
                CertViewState.viewState.deleted = true;
                CertViewState.viewState.activeErrorMessageKey = 'error';
                $scope.cert = undefined;
            });

            $rootScope.$on('saveRequest', function($event, deferred) {


                // Mark form as saved, will be marked as not saved if saving fails.
                $scope.certForm.$setPristine();

                var intygSaveRequest = {
                    intygsId      : $scope.certMeta.intygId,
                    intygsTyp     : $scope.certMeta.intygType,
                    cert          : $scope.cert,
                    saveComplete  : $q.defer()
                };

                intygSaveRequest.saveComplete.promise.then(function(result) {

                    // save success
                    viewState.isComplete = result.isComplete;
                    viewState.common.viewState.validationMessages = result.validationMessages;
                    viewState.common.viewState.validationMessagesGrouped = result.validationMessagesGrouped;
                    viewState.common.viewState.saveErrorMessageKey = null;

                }, function(result) {
                    // save failed
                    $scope.certForm.$setDirty();
                    viewState.common.viewState.saveErrorMessageKey = result.errorMessageKey;
                });

                deferred.resolve(intygSaveRequest);
            });

        }]);
