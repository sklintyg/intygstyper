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

angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form2Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            // --- form2
            $scope.$watch('viewState.intygModel.hypoglykemier.teckenNedsattHjarnfunktion',
                function(forekommerTeckenNedsattHjarnfunktion) {
                    if (!forekommerTeckenNedsattHjarnfunktion && viewState.intygModel.hypoglykemier) {
                        viewState.intygModel.updateToAttic('hypoglykemier.saknarFormagaKannaVarningstecken');
                        viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomst');
                        viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstTrafiken');

                        viewState.intygModel.hypoglykemier.saknarFormagaKannaVarningstecken = undefined;
                        viewState.intygModel.hypoglykemier.allvarligForekomst = undefined;
                        viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken = undefined;
                    } else {
                        viewState.intygModel.restoreFromAttic('hypoglykemier.saknarFormagaKannaVarningstecken');
                        viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomst');
                        viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstTrafiken');
                    }
                }, true);

            $scope.$watch('viewState.intygModel.hypoglykemier.allvarligForekomst', function(haftAllvarligForekomst) {
                if (!haftAllvarligForekomst && viewState.intygModel.hypoglykemier) {
                    viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstBeskrivning');
                    viewState.intygModel.hypoglykemier.allvarligForekomstBeskrivning = undefined;
                } else {
                    viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstBeskrivning');
                }
            }, true);
            $scope.$watch('viewState.intygModel.hypoglykemier.allvarligForekomstTrafiken', function(haftAllvarligForekomstTrafiken) {
                if (!haftAllvarligForekomstTrafiken && viewState.intygModel.hypoglykemier) {
                    viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstTrafikBeskrivning');
                    viewState.intygModel.hypoglykemier.allvarligForekomstTrafikBeskrivning = undefined;
                } else {
                    viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstTrafikBeskrivning');
                }
            }, true);

            // hypoglykemier.allvarligForekomstVakenTidObservationstid
            $scope.$watch('viewState.intygModel.hypoglykemier.allvarligForekomstVakenTid', function(haftAllvarligForekomstVakenTid) {
                if (!haftAllvarligForekomstVakenTid && viewState.intygModel.hypoglykemier) {
                    viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                    viewState.intygModel.hypoglykemier.allvarligForekomstVakenTidObservationstid = undefined;
                } else {
                    viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                }
            }, true);
            // --- form2

        }]);
