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

angular.module('fk7263').controller('fk7263.EditCert.Form5Ctrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService', 'common.fmbViewState', 'common.UtkastValidationService',
        function($scope, $log, viewState, fmbViewState, UtkastValidationService) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;

            $scope.viewState = viewState;

            $scope.fmbViewState = fmbViewState;

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        // 5. funktionsnedsättning
                        model.updateToAttic(model.properties.form5);
                        model.clear(model.properties.form5);
                    } else if(model.isInAttic(model.properties.form5)){
                        model.restoreFromAttic(model.properties.form5);
                    }
                }
            });

            $scope.validate = function() {
                UtkastValidationService.validate(model);
            };
        }]);
