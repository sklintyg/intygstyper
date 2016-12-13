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

angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form1Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            // --- form1
            $scope.$watch('viewState.intygModel.diabetes.insulin', function(behandlasMedInsulin) {
                if (!behandlasMedInsulin && viewState.intygModel.diabetes) {
                    viewState.intygModel.updateToAttic('diabetes.insulinBehandlingsperiod');
                    viewState.intygModel.diabetes.insulinBehandlingsperiod = null;

                } else {
                    viewState.intygModel.restoreFromAttic('diabetes.insulinBehandlingsperiod');
                }
            }, true);
            // --- form1

        }]);
