angular.module('luse').controller('sjukersattning.EditCertCtrl',
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$stateParams', '$q',
        'common.UtkastService', 'common.UserModel', 'common.DateUtilsService', 'common.UtilsService',
        'sjukersattning.Domain.IntygModel', 'sjukersattning.EditCertCtrl.ViewStateService',
        'common.anchorScrollService',
        function($rootScope, $anchorScroll, $filter, $location, $scope, $log, $timeout, $stateParams, $q,
            UtkastService, UserModel, dateUtils, utils, IntygModel, viewState, anchorScrollService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            // create a new intyg model and reset all viewStates
            viewState.reset();
            $scope.viewState = viewState;

            // Page states
            $scope.user = UserModel;

            $scope.viewState.prototypes = {
                selected : null,
                testModeActive : true,
                prototypTemplates: [{
                    id: 'default', label: 'default'
                }, {
                    id: 'prototype0', label: 'prototype1'
                }, {
                    id: 'prototype1', label: 'prototype2'
                }, {
                    id: 'prototype2', label: 'prototype3'
                }]
            };
            $scope.viewState.prototypes.selected = $scope.viewState.prototypes.prototypTemplates[0].id;


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
            UtkastService.load(viewState);

            $scope.$on('saveRequest', function($event, saveDeferred) {
                $scope.certForm.$setPristine();
                var intygState = {
                    viewState: viewState,
                    formFail: function() {
                        $scope.certForm.$setDirty();
                    }
                };
                saveDeferred.resolve(intygState);
            });

            $scope.$on('$destroy', function() {
                if (!$scope.certForm.$dirty) {
                    $scope.destroyList();
                }
            });

            $scope.destroyList = function() {
                viewState.clearModel();
            };

        }]);
