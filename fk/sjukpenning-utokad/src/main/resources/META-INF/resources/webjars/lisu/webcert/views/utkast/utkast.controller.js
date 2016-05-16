angular.module('lisu').controller('sjukpenning-utokad.EditCertCtrl',
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$stateParams', '$q',
        'common.UtkastService', 'common.UserModel', 'common.DateUtilsService', 'common.UtilsService', 'common.ObjectHelper',
        'sjukpenning-utokad.Domain.IntygModel', 'sjukpenning-utokad.EditCertCtrl.ViewStateService',
        'common.anchorScrollService', 'sjukpenning-utokad.FormFactory', 'common.fmbService', 'common.fmb.ViewStateService',
        function($rootScope, $anchorScroll, $filter, $location, $scope, $log, $timeout, $stateParams, $q,
            UtkastService, UserModel, dateUtils, utils, ObjectHelper, IntygModel, viewState, anchorScrollService, formFactory,
            fmbService, fmbViewState) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            // create a new intyg model and reset all viewStates
            viewState.reset();
            $scope.viewState = viewState;

            // Page states
            $scope.user = UserModel;

            $scope.categoryNames = formFactory.getCategoryNames();

            /****************************************************************************
             * Exposed interaction functions to view
             ****************************************************************************/

            $scope.scrollTo = function(message) {
                anchorScrollService.scrollTo('anchor.' + message);
            };

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/

            // Get the certificate draft from the server.
            UtkastService.load(viewState).then(function(intygModel) {
                $log.debug("intygModel loaded");
                $log.debug(intygModel);
                if(angular.isArray(intygModel.diagnoser) && intygModel.diagnoser[0].diagnosKod) {
                    fmbService.getFMBHelpTextsByCodeAndType(intygModel.diagnoser[0].diagnosKod, 'LISU').then(

                        function (formData) {
                            fmbViewState.setState(formData, intygModel.diagnoser[0].diagnosKod);
                            $log.debug('fmbViewState from utkast controller');
                            $log.debug(fmbViewState);
                        },
                        function (rejectionData) {
                            $log.debug('fmbViewState from utkast controller - Error searching fmb help text');
                            $log.debug(rejectionData);
                            fmbViewState.reset();
                        }
                    );
                } else{
                    $log.debug('intygModel does not have diagnose code set - resetting fmbViewState');
                    fmbViewState.reset();
                }


                if (viewState.common.textVersionUpdated) {
                    $scope.certForm.$setDirty();
                }
            });

            $scope.$on('saveRequest', function($event, saveDeferred) {
                $scope.certForm.$setPristine();
                var intygState = {
                    viewState : viewState,
                    formFail : function(){
                        $scope.certForm.$setDirty();
                    }
                };
                saveDeferred.resolve(intygState);
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
