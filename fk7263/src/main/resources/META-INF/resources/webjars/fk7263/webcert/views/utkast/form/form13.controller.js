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

angular.module('fk7263').controller('fk7263.EditCert.Form13Ctrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService', 'common.UtkastValidationService',
        function($scope, $log, viewState, UtkastValidationService) {
            'use strict';
            $scope.model = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.inputLimits = {
                ovrigt: 360 // = combined field 13 (and dependencies that end up in field 13) limit
            };
            $scope.teckenKvar = function(){
                return viewState.inputLimits.ovrigt - viewState.getTotalOvrigtLength();
            };

            $scope.validate = function() {
                UtkastValidationService.validate($scope.model);
            };

        }]);
