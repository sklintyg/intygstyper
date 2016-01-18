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
    ['$rootScope', '$anchorScroll', '$filter', '$location', '$scope', '$log', '$timeout', '$stateParams', '$q',
        'common.UtkastService', 'common.UserModel', 'fk7263.diagnosService',
        'common.DateUtilsService', 'common.UtilsService', 'fk7263.Domain.IntygModel',
        'fk7263.EditCertCtrl.ViewStateService', 'common.anchorScrollService', 'fk7263.fmb.ViewStateService', 'fk7263.fmbService',
        function($rootScope, $anchorScroll, $filter, $location, $scope, $log, $timeout, $stateParams, $q,
            UtkastService, UserModel, diagnosService,
            dateUtils, utils, IntygModel, viewState, anchorScrollService, fmbViewState, fmbService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            // create a new intyg model and reset all viewStates
            viewState.reset();
            $scope.viewState = viewState;

            // Page states
            $scope.user = UserModel.user;

            /****************************************************************************
             * Exposed interaction functions to view
             ****************************************************************************/

            $scope.scrollTo = function(message) {
                anchorScrollService.scrollTo('anchor.' + message);
            };

            /**************************************************************************
             * Load certificate and setup form / Constructor ...
             **************************************************************************/

            $scope.$on('saveRequest', function($event, saveDefered) {
                $scope.certForm.$setPristine();
                var intygState = {
                    viewState : viewState,
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
                fmbViewState.reset();
            });

            $scope.destroyList = function(){
                viewState.clearModel();
            };

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
                }
            );

        }]);
