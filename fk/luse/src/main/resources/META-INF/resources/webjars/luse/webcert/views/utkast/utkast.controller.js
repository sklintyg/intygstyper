angular.module('luse').controller('luse.EditCertCtrl',
    ['$rootScope', '$filter', '$location', '$scope', '$log', '$timeout', '$stateParams', '$q',
        'common.UtkastService', 'common.UserModel', 'common.DateUtilsService', 'common.UtilsService',
        'luse.Domain.IntygModel', 'luse.EditCertCtrl.ViewStateService',
        'luse.FormFactory',
        function($rootScope, $filter, $location, $scope, $log, $timeout, $stateParams, $q,
            UtkastService, UserModel, dateUtils, utils, IntygModel, viewState, formFactory) {
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

            $scope.categoryNames = formFactory.getCategoryNames();

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/

            // Get the certificate draft from the server.
            UtkastService.load(viewState).then(function() {
                if (viewState.common.textVersionUpdated) {
                    $scope.certForm.$setDirty();
                }
            });

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
