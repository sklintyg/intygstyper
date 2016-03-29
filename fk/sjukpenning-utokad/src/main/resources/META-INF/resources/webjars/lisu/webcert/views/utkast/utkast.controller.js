angular.module('lisu').controller('sjukpenning-utokad.EditCertCtrl',
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$stateParams', '$q',
        'common.UtkastService', 'common.UserModel', 'common.DateUtilsService', 'common.UtilsService',
        'sjukpenning-utokad.Domain.IntygModel', 'sjukpenning-utokad.EditCertCtrl.ViewStateService',
        'common.anchorScrollService', 'sjukpenning-utokad.FormFactory', 'common.fmbService', 'common.fmbViewState',
        function($rootScope, $anchorScroll, $filter, $location, $scope, $log, $timeout, $stateParams, $q,
            UtkastService, UserModel, dateUtils, utils, IntygModel, viewState, anchorScrollService, formFactory,
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
                if(intygModel.diagnosKod) {
                    fmbService.getFMBHelpTextsByCode(intygModel.diagnosKod).then(
                        function (formData) {
                            fmbViewState.setState(formData, intygModel.diagnosKod);
                        },
                        function (rejectionData) {
                            $log.debug('Error searching fmb help text');
                            $log.debug(rejectionData);
                            return [];
                        }
                    );
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

/*
            UtkastService.load(viewState).then(function(intygModel) {
                    if(intygModel.diagnosKod) {
                        fmbService.getFMBHelpTextsByCode(intygModel.diagnosKod).then(
                            function (formData) {
                                fmbViewState.setState(formData, intygModel.diagnosKod);
                            },
                            function (rejectionData) {
                                $log.debug('Error searching fmb help text');
                                $log.debug(rejectionData);
                                return [];
                            }
                        );
                    }
                }
            );
   */
            $scope.$on('$destroy', function() {
                if(!$scope.certForm.$dirty){
                    $scope.destroyList();
                }
            });

            $scope.destroyList = function(){
                viewState.clearModel();
            };

        }]);
