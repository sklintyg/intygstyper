angular.module('lisjp').controller('lisjp.EditCertCtrl',
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$stateParams', '$q',
        'common.UtkastService', 'common.UserModel', 'common.DateUtilsService', 'common.UtilsService',
        'lisjp.Domain.IntygModel', 'lisjp.EditCertCtrl.ViewStateService',
        'common.anchorScrollService', 'lisjp.FormFactory', 'common.fmbService', 'common.fmbViewState',

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
                if (viewState.common.textVersionUpdated) {
                    $scope.certForm.$setDirty();
                }

                fmbService.updateFmbTextsForAllDiagnoses(intygModel.diagnoser);
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
                fmbViewState.reset();
            });

            $scope.destroyList = function(){
                viewState.clearModel();
            };

        }]);
