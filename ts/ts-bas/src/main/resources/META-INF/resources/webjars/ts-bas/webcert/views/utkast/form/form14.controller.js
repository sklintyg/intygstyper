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

angular.module('ts-bas').controller('ts-bas.Utkast.Form14Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.sjukhusvard.sjukhusEllerLakarkontakt', function(vardatsPaSjukhus) {
                if (!vardatsPaSjukhus && viewState.intygModel.sjukhusvard) {
                    viewState.intygModel.updateToAttic('sjukhusvard.tidpunkt');
                    viewState.intygModel.updateToAttic('sjukhusvard.vardinrattning');
                    viewState.intygModel.updateToAttic('sjukhusvard.anledning');
                    viewState.intygModel.sjukhusvard.tidpunkt = '';
                    viewState.intygModel.sjukhusvard.vardinrattning = '';
                    viewState.intygModel.sjukhusvard.anledning = '';
                } else {
                    viewState.intygModel.restoreFromAttic('sjukhusvard.tidpunkt');
                    viewState.intygModel.restoreFromAttic('sjukhusvard.vardinrattning');
                    viewState.intygModel.restoreFromAttic('sjukhusvard.anledning');
                }
            }, true);

        }]);
