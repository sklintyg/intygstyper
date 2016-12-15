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
    [ '$location', '$q', '$rootScope', '$scope', '$timeout', '$window',
        'common.UtkastService', 'common.UserModel',
        'ts-bas.Domain.IntygModel',
        'ts-bas.UtkastController.ViewStateService',
        function($location, $q, $rootScope, $scope, $timeout, $window,
            UtkastService, UserModel, IntygModel, viewState) {
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
            $scope.viewState = viewState.reset();
            $scope.user = UserModel.user;

            $scope.categoryNames = {
                99: 'intygAvser',
                100: 'identitet',
                1: 'syn',
                2: 'horselBalans',
                3: 'funktionsnedsattning',
                4: 'hjartKarl',
                5: 'diabetes',
                6: 'neurologi',
                7: 'medvetandestorning',
                8: 'njurar',
                9: 'kognitivt',
                10: 'somnVakenhet',
                11: 'narkotikaLakemedel',
                12: 'psykiskt',
                13: 'utvecklingsstorning',
                14: 'sjukhusvard',
                15: 'medicinering',
                16: 'ovrigt',
                101: 'bedomning'
            };

            /*************************************************************************
             * Ng-change and watches updating behaviour in form (try to get rid of these or at least make them consistent)
             *************************************************************************/

            $scope.$watch('viewState.intygModel', function() {
                viewState.updateKravYtterligareUnderlag();
            }, true);

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
