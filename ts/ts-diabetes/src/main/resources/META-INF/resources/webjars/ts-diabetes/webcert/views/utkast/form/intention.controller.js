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

angular.module('ts-diabetes').controller('ts-diabetes.Utkast.IntentionController',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            // --- intention
            $scope.$watch('viewState.intygModel.intygAvser.korkortstyp', function(valdaKorkortstyper) {
                if (viewState.intygModel.intygAvser && viewState.intygModel.intygAvser.korkortstyp) {
                    var targetTypes = ['C1', 'C1E', 'C', 'CE', 'D1', 'D1E', 'D', 'DE', 'TAXI'];
                    var visaKorkortd = false;
                    for (var i = 0; i < valdaKorkortstyper.length; i++) {
                        for (var j = 0; j < targetTypes.length; j++) {
                            if (valdaKorkortstyper[i].type === targetTypes[j] && valdaKorkortstyper[i].selected) {
                                visaKorkortd = true;
                                break;
                            }
                        }
                    }
                    $scope.viewState.korkortd = visaKorkortd;
                    if (visaKorkortd) {
                        viewState.intygModel.restoreFromAttic('hypoglykemier.egenkontrollBlodsocker');
                        viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTid');
                        viewState.intygModel.restoreFromAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                        viewState.intygModel.restoreFromAttic('bedomning.lamplighetInnehaBehorighet');
                    } else {
                        viewState.intygModel.updateToAttic('hypoglykemier.egenkontrollBlodsocker');
                        viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstVakenTid');
                        viewState.intygModel.updateToAttic('hypoglykemier.allvarligForekomstVakenTidObservationstid');
                        viewState.intygModel.updateToAttic('bedomning.lamplighetInnehaBehorighet');
                        viewState.intygModel.hypoglykemier.egenkontrollBlodsocker = undefined;
                        viewState.intygModel.hypoglykemier.allvarligForekomstVakenTid = undefined;
                        viewState.intygModel.bedomning.lamplighetInnehaBehorighet = undefined;
                    }
                }
            }, true);
            // --- intention

        }]);
