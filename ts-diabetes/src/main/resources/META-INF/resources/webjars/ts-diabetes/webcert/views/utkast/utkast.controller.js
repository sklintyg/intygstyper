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

angular.module('ts-diabetes').controller('ts-diabetes.UtkastController',
    ['$location', '$log', '$q', '$rootScope', '$scope', '$timeout', '$window', 'common.UtkastService', 'common.UserModel',
        'ts-diabetes.Domain.IntygModel', 'ts-diabetes.UtkastController.ViewStateService', 'common.DateUtilsService',
        'common.anchorScrollService',
        function($location, $log, $q, $rootScope, $scope, $timeout, $window, UtkastService, UserModel,
            IntygModel, viewState, dateUtils, anchorScrollService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            viewState.reset();
            $scope.viewState = viewState;

            // Page state
            $scope.user = UserModel.user;

            /******************************************************************************************
             * Private support functions
             ******************************************************************************************/

           /******************************************************************************************
             * Watches
             ******************************************************************************************/

                // Watch changes to the form and make sure that other form elements that are dependent on the changed
                // element is updated correctly.

            // ---------------------------------------------------------------------------------------------------------


            /******************************************************************************************
             * Exposed interaction
             ******************************************************************************************/

            $scope.scrollTo = function(message) {
                anchorScrollService.scrollTo('anchor.' + message);
            };

            /**************************************************************************
             * Load certificate and setup form
             **************************************************************************/

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

            // Sätt upp lyssnare för 'intyg.loaded' innan load anropas, säkerställer att lyssnaren hunnit registreras när load körs.
            // Get the certificate draft from the server.
            UtkastService.load(viewState);

        }]);
