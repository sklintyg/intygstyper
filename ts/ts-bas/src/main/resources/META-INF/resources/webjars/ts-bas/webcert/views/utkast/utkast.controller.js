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

angular.module('ts-bas').controller('ts-bas.UtkastController',
    [ '$anchorScroll', '$location', '$q', '$rootScope', '$scope', '$timeout', '$window',
        'common.UtkastService', 'common.UserModel',
        'ts-bas.Domain.IntygModel',
        'ts-bas.UtkastController.ViewStateService', 'common.anchorScrollService',
        function($anchorScroll, $location, $q, $rootScope, $scope, $timeout, $window,
            UtkastService, UserModel, IntygModel, viewState, anchorScrollService) {
            'use strict';

            /**********************************************************************************
             * Default state
             **********************************************************************************/

            $scope.viewState = viewState.reset();
            $scope.notifieringVidarebefordrad = viewState.draftModel.vidarebefordrad; // temporary hack. let view take from viewstate
            $scope.user = UserModel.user;

            /***************************************************************************
             * Private controller support functions
             ***************************************************************************/


            /*************************************************************************
             * Ng-change and watches updating behaviour in form (try to get rid of these or at least make them consistent)
             *************************************************************************/

            $scope.$watch('viewState.intygModel', function() {
                viewState.updateKravYtterligareUnderlag();
            }, true);


            /****************************************************************************
             * Exposed interaction functions to view
             ****************************************************************************/

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

            // Get the certificate draft from the server.
            UtkastService.load(viewState).then(function() {
                if (viewState.common.textVersionUpdated) {
                    $scope.certForm.$setDirty();
                }
            });

        }]);
