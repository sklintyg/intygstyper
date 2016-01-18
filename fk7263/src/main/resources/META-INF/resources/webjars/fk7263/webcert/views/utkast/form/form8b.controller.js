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

angular.module('fk7263').controller('fk7263.EditCert.Form8bCtrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService',
        'common.DateRangeService', 'fk7263.fmb.ViewStateService',
        function($scope, $log, viewState, DateRangeService, fmbViewState) {
            'use strict';
            // private vars

            // scope
            $scope.model = viewState.intygModel;
            $scope.viewState = viewState;
            $scope.fmb = fmbViewState.state;

            // 1. onload
            // 2. on check change
            // 3. on manual change.

            var _dateRangeService = DateRangeService.FromTos.build(['nedsattMed25','nedsattMed50','nedsattMed75','nedsattMed100']);


            // 8b. Arbetsförmåga date management
            $scope.field8b = {
                nedsattMed25 : _dateRangeService.nedsattMed25,
                nedsattMed50 : _dateRangeService.nedsattMed50,
                nedsattMed75 : _dateRangeService.nedsattMed75,
                nedsattMed100 : _dateRangeService.nedsattMed100,
                onChangeWorkStateCheck : function(nedsattModelName) {
                    $log.debug('------------------------ onChangeWorkStateCheck');

                    _dateRangeService[nedsattModelName].check();

                    if (!$scope.field8b[nedsattModelName].workState) {
                        viewState.intygModel[nedsattModelName + 'Beskrivning'] = undefined;
                    }

                },
                info : _dateRangeService
            };

            $scope.info = _dateRangeService;


            $scope.$watch('field8b.nedsattMed25.workState', function(newVal, oldVal) {
                $log.debug('workstate : newVal:' + newVal + ', oldVal:' + oldVal);
            });

            var doneLoading = false;
            $scope.$watch(function() {

                return !!(viewState.common.doneLoading &&
                    angular.isObject($scope.form8b.nedsattMed25from) &&
                    angular.isObject($scope.form8b.nedsattMed25tom) &&
                    angular.isObject($scope.form8b.nedsattMed50from) &&
                    angular.isObject($scope.form8b.nedsattMed50tom) &&
                    angular.isObject($scope.form8b.nedsattMed75from) &&
                    angular.isObject($scope.form8b.nedsattMed75tom) &&
                    angular.isObject($scope.form8b.nedsattMed100from) &&
                    angular.isObject($scope.form8b.nedsattMed100tom));

            }, function(newVal, oldVal) {
                if (doneLoading) {
                    return;
                }
                if (newVal) {
                    _dateRangeService.linkFormAndModel($scope.form8b, viewState.intygModel, $scope);
                    doneLoading = true;
                }
            });

        }]);
