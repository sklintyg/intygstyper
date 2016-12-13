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

angular.module('ts-bas').controller('ts-bas.Utkast.Form11Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.narkotikaLakemedel.teckenMissbruk || viewState.intygModel.narkotikaLakemedel.foremalForVardinsats',
                function(shown) {
                    if (shown) {
                        viewState.intygModel.restoreFromAttic('narkotikaLakemedel.provtagningBehovs');
                    } else {
                        viewState.intygModel.updateToAttic('narkotikaLakemedel.provtagningBehovs');
                        viewState.intygModel.narkotikaLakemedel.provtagningBehovs = undefined;
                    }
                }, true);

            $scope.$watch('viewState.intygModel.narkotikaLakemedel.lakarordineratLakemedelsbruk', function(anvanderOrdineradNarkotika) {
                if (!anvanderOrdineradNarkotika && viewState.intygModel.narkotikaLakemedel) {
                    viewState.intygModel.updateToAttic('narkotikaLakemedel.lakemedelOchDos');
                    viewState.intygModel.narkotikaLakemedel.lakemedelOchDos = '';
                } else {
                    viewState.intygModel.restoreFromAttic('narkotikaLakemedel.lakemedelOchDos');
                }
            }, true);

        }]);
