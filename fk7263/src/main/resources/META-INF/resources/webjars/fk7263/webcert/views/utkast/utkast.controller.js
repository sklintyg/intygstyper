/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

angular.module('fk7263').controller('fk7263.EditCertCtrl',
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$state', '$stateParams', '$q',
        'common.UtkastService', 'common.UserModel', 'fk7263.diagnosService',
        'common.DateUtilsService', 'common.UtilsService', 'fk7263.Domain.IntygModel',
        'fk7263.EditCertCtrl.ViewStateService', 'common.fmbViewState', 'common.fmbService',
        'common.ObjectHelper', 'common.IntygProxy', 'common.IntygHelper',
        function($rootScope, $anchorScroll, $filter, $location, $scope, $log, $timeout, $state, $stateParams, $q,
            UtkastService, UserModel, diagnosService, dateUtils, utils, IntygModel, viewState,
            fmbViewState, fmbService, ObjectHelper, IntygProxy, IntygHelper) {
            'use strict';

            /**********************************************************************************
             * Auto-resize textareas
             **********************************************************************************/
            $('.edit-form').on( 'keydown', 'textarea', function (e) {
                $(this).css('height', 'auto' );
                $(this).height( this.scrollHeight );
            });
            $('.edit-form').find('textarea').keydown();

            /**********************************************************************************
             * Default state
             **********************************************************************************/

                // create a new intyg model and reset all viewStates
            viewState.reset();
            $scope.viewState = viewState;

            // Page states
            $scope.user = UserModel.user;

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/

            $scope.$on('saveRequest', function($event, saveDefered) {
                $scope.certForm.$setPristine();
                var intygState = {
                    viewState: viewState,
                    formFail: function() {
                        $scope.certForm.$setDirty();
                    }
                };
                saveDefered.resolve(intygState);
            });

            $scope.$on('$destroy', function() {
                if (!$scope.certForm.$dirty) {
                    $scope.destroyList();
                }
                fmbViewState.reset();
            });

            $scope.destroyList = function() {
                viewState.clearModel();
            };

            // Get the certificate draft from the server.
            UtkastService.load(viewState).then(function(intygModel) {

                fmbService.updateFmbTextsForAllDiagnoses([
                    {diagnosKod: intygModel.diagnosKod},
                    {diagnosKod: intygModel.diagnosKod2},
                    {diagnosKod: intygModel.diagnosKod3}
                ]);

                // Load parentIntyg to feed fragasvar component with load event
                if (ObjectHelper.isDefined(intygModel.grundData.relation) &&
                    ObjectHelper.isDefined(intygModel.grundData.relation.relationIntygsId) &&
                    ObjectHelper.isDefined(intygModel.grundData.relation.meddelandeId)) {
                    IntygProxy.getIntyg(intygModel.grundData.relation.relationIntygsId, viewState.common.intyg.type, false,
                        function(result) {
                            if (result !== null && result !== '') {
                                var parentIntyg = result.contents;
                                var intygMeta = {
                                    isSent: IntygHelper.isSentToTarget(result.statuses, 'FK'),
                                    isRevoked: IntygHelper.isRevoked(result.statuses),
                                    forceUseProvidedIntyg: true,
                                    meddelandeId: intygModel.grundData.relation.meddelandeId,
                                    type: viewState.common.intyg.type
                                };
                                $rootScope.$emit('ViewCertCtrl.load', parentIntyg, intygMeta);
                            } else {
                                $rootScope.$emit('ViewCertCtrl.load', null, null);
                            }
                        }, function(error) {
                            $rootScope.$emit('ViewCertCtrl.load', null, null);
                        });
                } else {
                    // Failed to load parent intyg. Tell frågasvar
                    $rootScope.$broadcast('ViewCertCtrl.load', null, {
                        isSent: false,
                        isRevoked: false
                    });
                }
            }, function(error) {

                if(typeof error === 'object') {
                    $log.error('Failed to load utkast. Reason: ' + error.errorCode + ': ' + error.message);
                }

                // Failed to load parent intyg. Tell frågasvar
                $rootScope.$broadcast('ViewCertCtrl.load', null, {
                    isSent: false,
                    isRevoked: false
                });
            });

            $scope.gotoRelatedIntyg = function(intyg) {
                if (intyg.status === 'SIGNED') {
                    $state.go('webcert.intyg.fk.fk7263', {certificateId: intyg.intygsId});
                }
                else {
                    $state.go('fk7263-edit', {certificateId: intyg.intygsId});
                }
            };

        }]);
