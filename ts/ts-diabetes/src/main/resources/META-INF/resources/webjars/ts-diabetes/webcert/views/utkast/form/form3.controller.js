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

angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form3Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            // --- form3
            $scope.$watch('viewState.intygModel.syn.separatOgonlakarintyg', function(separatOgonlakarintyg) {
                if(separatOgonlakarintyg !== undefined) {
                    if (separatOgonlakarintyg && viewState.intygModel.syn) {
                        viewState.intygModel.updateToAttic('syn.synfaltsprovningUtanAnmarkning');
                        viewState.intygModel.updateToAttic('syn.hoger');
                        viewState.intygModel.updateToAttic('syn.vanster');
                        viewState.intygModel.updateToAttic('syn.binokulart');
                        viewState.intygModel.updateToAttic('syn.diplopi');

                        viewState.intygModel.clear('syn.synfaltsprovningUtanAnmarkning');
                        viewState.intygModel.clear('syn.hoger');
                        viewState.intygModel.clear('syn.vanster');
                        viewState.intygModel.clear('syn.binokulart');
                        viewState.intygModel.clear('syn.diplopi');
                    } else {
                        viewState.intygModel.restoreFromAttic('syn.synfaltsprovningUtanAnmarkning');
                        viewState.intygModel.restoreFromAttic('syn.hoger');
                        viewState.intygModel.restoreFromAttic('syn.vanster');
                        viewState.intygModel.restoreFromAttic('syn.binokulart');
                        viewState.intygModel.restoreFromAttic('syn.diplopi');
                    }
                }
            }, true);
            // --- form3

        }]);
