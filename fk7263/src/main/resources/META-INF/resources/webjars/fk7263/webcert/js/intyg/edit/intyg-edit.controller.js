angular.module('fk7263').controller('fk7263.EditCertCtrl',
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$stateParams', '$q',
        'common.CertificateService', 'common.ManageCertView', 'common.UserModel',
        'common.intygNotifyService', 'fk7263.diagnosService', 'common.DateUtilsService', 'common.UtilsService',
        'fk7263.Domain.IntygModel', 'fk7263.EditCertCtrl.ViewStateService', 'common.anchorScrollService',
        function($rootScope, $anchorScroll, $filter, $location, $scope, $log, $timeout, $stateParams, $q,
            CertificateService, ManageCertView, UserModel, intygNotifyService, diagnosService,
            dateUtils, utils, IntygModel, viewState, anchorScrollService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/
            // create a new intyg model and reset all viewStates
            viewState.reset();
            $scope.viewState = viewState;

            // Page states
            $scope.user = UserModel;

            $scope.today = new Date(); // TODO: to viewstate
            $scope.today.setHours(0, 0, 0, 0); // reset time to increase comparison accuracy (using new Date() also sets time)
            $scope.focusFirstInput = false; // TODO: to common viewstate

            // Intyg state
            $scope.cert = viewState.intygModel;


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
                    intygId : viewState.intygModel.id,
                    intygType: viewState.common.intyg.type,
                    intygVersion: viewState.draftModel.version,
                    vidarebefordrad: viewState.draftModel.vidarebefordrad,
                    updateFunc: function(vidarebefordrad, version) {
                        viewState.draftModel.vidarebefordrad = vidarebefordrad;
                        if (version) {
                           viewState.draftModel.version = version;
                        }
                    }
                };
                intygNotifyService.forwardIntyg(utkastNotifyRequest);
            };

            $scope.onVidarebefordradChange = function() {

                var utkastNotifyRequest = {
                    intygId : viewState.intygModel.id,
                    intygType: viewState.common.intyg.type,
                    intygVersion: viewState.draftModel.version,
                    vidarebefordrad: viewState.draftModel.vidarebefordrad,
                    updateFunc: function(vidarebefordrad, version) {
                        viewState.draftModel.vidarebefordrad = vidarebefordrad;
                        if (version) {
                            viewState.draftModel.version = version;
                        }
                    }
                };
                intygNotifyService.onForwardedChange(utkastNotifyRequest);
            };

            /**
             * Action to sign the certificate draft and return to Webcert again.
             */
            $scope.sign = function() {
                ManageCertView.signera(viewState.common.intyg.type, viewState.draftModel.version).then(
                    function(result) {
                        if (result.newVersion) {
                            viewState.draftModel.version = result.newVersion;
                        }
                    }
                );
            };

            $scope.scrollTo = function(message) {
                anchorScrollService.scrollTo('anchor.' + message);
            };

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/

            // Get the certificate draft from the server.
            ManageCertView.load(viewState);

            $scope.$on('saveRequest', function($event, saveDefered) {
                $scope.certForm.$setPristine();
                var intygState = {
                    viewState     : viewState,
                    formFail : function(){
                        $scope.certForm.$setDirty();
                    }
                };
                saveDefered.resolve(intygState);
            });

            $scope.$on('$destroy', function() {
                if(!$scope.certForm.$dirty){
                    $scope.destroyList();
                }
            });

            $scope.destroyList = function(){
                viewState.clearModel();
            };

        }]);
