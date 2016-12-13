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

angular.module('ts-bas').controller('ts-bas.Utkast.Form5Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.diabetes.harDiabetes', function(harDiabetes) {
                if (!harDiabetes && viewState.intygModel.hjartKarl) {
                    viewState.intygModel.updateToAttic('diabetes.diabetesTyp');
                    viewState.intygModel.diabetes.diabetesTyp = undefined;
                } else {
                    viewState.intygModel.restoreFromAttic('diabetes.diabetesTyp');
                }
            }, true);

            $scope.$watch('viewState.intygModel.diabetes.diabetesTyp', function(diabetesTyp) {
                if (diabetesTyp !== 'DIABETES_TYP_2' && viewState.intygModel.diabetes) {
                    viewState.intygModel.updateToAttic('diabetes.kost');
                    viewState.intygModel.updateToAttic('diabetes.tabletter');
                    viewState.intygModel.updateToAttic('diabetes.insulin');
                    viewState.intygModel.diabetes.kost = undefined;
                    viewState.intygModel.diabetes.tabletter = undefined;
                    viewState.intygModel.diabetes.insulin = undefined;
                } else {
                    viewState.intygModel.restoreFromAttic('diabetes.kost');
                    viewState.intygModel.restoreFromAttic('diabetes.tabletter');
                    viewState.intygModel.restoreFromAttic('diabetes.insulin');
                }
            }, true);

        }]);
