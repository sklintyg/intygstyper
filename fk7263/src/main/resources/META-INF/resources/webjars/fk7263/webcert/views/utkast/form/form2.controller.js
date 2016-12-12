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

angular.module('fk7263').controller('fk7263.EditCert.Form2Ctrl',
    ['$scope', '$log', '$http', 'fk7263.EditCertCtrl.ViewStateService', 'fk7263.diagnosService', 'common.fmbService', 'common.fmbViewState',
        'fk7263.EditCertCtrl.Helper', 'common.MonitoringLogService', 'common.UtkastValidationService',
        function($scope, $log, $http, viewState, diagnosService, fmbService, fmbViewState, helper, monitoringService, UtkastValidationService) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;

            $scope.viewState = viewState;

            $scope.fmbViewState = fmbViewState;

            $scope.viewModel = {
                diagnosKodverk : ''
            };

            $scope.diagnosKodLoading = [];
            $scope.diagnosKodNoResults = [];

            var diagnosKodverkStates = {ICD_10_SE:'ICD_10_SE',KSH_97_P:'KSH_97_P'};

            function updateFmbTextsForAllDiagnoses() {
                fmbService.updateFmbTextsForAllDiagnoses([
                    {diagnosKod: $scope.model.diagnosKod},
                    {diagnosKod: $scope.model.diagnosKod2},
                    {diagnosKod: $scope.model.diagnosKod3}
                ]);
            }

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }

                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        model.updateToAttic(model.properties.form2);
                        model.clear(model.properties.form2);
                    } else {
                        if(model.isInAttic(model.properties.form2)){
                            model.restoreFromAttic(model.properties.form2);
                            if(!model.diagnosKodsystem1){
                                setAllDiagnosKodverk( diagnosKodverkStates.ICD_10_SE );
                            }
                        }
                    }
                    updateFmbTextsForAllDiagnoses();
                }
            });

            $scope.$watch('viewState.common.doneLoading', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                if (newVal) {
                    if (!$scope.model.diagnosKodsystem1) {
                        setAllDiagnosKodverk( diagnosKodverkStates.ICD_10_SE );
                    } else {
                        setAllDiagnosKodverk( $scope.model.diagnosKodsystem1 );
                    }
                }
            });

            $scope.$watch('viewModel.diagnosKodverk', function(newVal, oldVal) {
                if(newVal === oldVal || oldVal === ''){
                    return;
                }
                monitoringService.diagnoskodverkChanged(viewState.intygModel.id, 'fk7263');
            });

            function setAllDiagnosKodverk(val){
                $scope.viewModel.diagnosKodverk = val;
                $scope.model.diagnosKodsystem1 = val;
                $scope.model.diagnosKodsystem2 = val;
                $scope.model.diagnosKodsystem3 = val;
            }

            /**
             *  Remove choices related to diagnoskoder when the choice changes to make sure
             */
            $scope.onChangeKodverk = function() {
                $scope.model.diagnosKod = undefined;
                $scope.model.diagnosBeskrivning1 = undefined;
                $scope.model.diagnosKod2 = undefined;
                $scope.model.diagnosBeskrivning2 = undefined;
                $scope.model.diagnosKod3 = undefined;
                $scope.model.diagnosBeskrivning3 = undefined;

                updateFmbTextsForAllDiagnoses();

                setAllDiagnosKodverk( $scope.viewModel.diagnosKodverk );
            };

            /**
             * Clear diagnose description if diagnose code is cleared
             */
            $scope.onChangeDiagnoseCode1 = function() {
                if (!$scope.form2['diagnose.code'].$viewValue) {
                    $scope.model.diagnosBeskrivning1 = undefined;
                    fmbService.updateFmbText(0, $scope.model.diagnosKod);
                }
                $scope.limitDiagnosBeskrivningField('diagnosKod1');
            };
            $scope.onChangeDiagnoseCode2 = function() {
                if (!$scope.form2['diagnose.codeOpt1'].$viewValue) {
                    $scope.model.diagnosBeskrivning2 = undefined;
                    fmbService.updateFmbText(1, $scope.model.diagnosKod2);
                }
                $scope.limitDiagnosBeskrivningField('diagnosKod2');
            };
            $scope.onChangeDiagnoseCode3 = function() {
                if (!$scope.form2['diagnose.codeOpt2'].$viewValue) {
                    $scope.model.diagnosBeskrivning3 = undefined;
                    fmbService.updateFmbText(2, $scope.model.diagnosKod3);
                }
                $scope.limitDiagnosBeskrivningField('diagnosKod3');
            };

            /**
             * Clear diagnose code if diagnose description is cleared
             */
            $scope.onChangeDiagnoseDescription1 = function() {
                if (!$scope.model.diagnosBeskrivning1) {
                    $scope.model.diagnosKod = undefined;
                }
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning1');
            };
            $scope.onChangeDiagnoseDescription2 = function() {
                if (!$scope.model.diagnosBeskrivning2) {
                    $scope.model.diagnosKod2 = undefined;
                }
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning2');
            };
            $scope.onChangeDiagnoseDescription3 = function() {
                if (!$scope.model.diagnosBeskrivning3) {
                    $scope.model.diagnosKod3 = undefined;
                }
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning3');
            };

            $scope.getDiagnoseCodes = function(codeSystem, val) {
                return diagnosService.searchByCode(codeSystem, val)
                    .then(function(response) {
                        if (response && response.data && response.data.resultat === 'OK') {
                            var result = response.data.diagnoser.map(function(item) {
                                return {
                                    value: item.kod,
                                    beskrivning: item.beskrivning,
                                    label: item.kod + ' | ' + item.beskrivning
                                };
                            });
                            if (result.length > 0) {
                                result[0].moreResults = response.data.moreResults;
                            }
                            return result;
                        }
                        else {
                            return [];
                        }
                    }, function(response) {
                        $log.debug('Error searching diagnose code');
                        $log.debug(response);
                        return [];
                    });
            };

            /**
             *
             * @param codeSystem
             * @param val
             * @returns {*}
             */
            $scope.searchDiagnoseByDescription = function(codeSystem, val) {
                return diagnosService.searchByDescription(codeSystem, val)
                    .then(function(response) {
                        if (response && response.data && response.data.resultat === 'OK') {
                            var result = response.data.diagnoser.map(function(item) {
                                return {
                                    value: item.kod,
                                    beskrivning: item.beskrivning,
                                    label: item.kod + ' | ' + item.beskrivning
                                };
                            });
                            if (result.length > 0) {
                                result[0].moreResults = response.data.moreResults;
                            }
                            return result;
                        }
                        else {
                            return [];
                        }
                    }, function(response) {
                        $log.debug('Error searching diagnose code');
                        $log.debug(response);
                        return [];
                    });
            };

            /**
             * User selects a diagnose code
             */
            $scope.onDiagnoseCode1Select = function($item) {
                $scope.model.diagnosBeskrivning1 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning1');
                fmbService.updateFmbText(0, $scope.model.diagnosKod);
                $scope.form2.$setDirty();
                model.updateToAttic(model.properties.form2);
            };
            $scope.onDiagnoseCode2Select = function($item) {
                $scope.model.diagnosBeskrivning2 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning2');
                fmbService.updateFmbText(1, $scope.model.diagnosKod2);
                $scope.form2.$setDirty();
                model.updateToAttic(model.properties.form2);
            };
            $scope.onDiagnoseCode3Select = function($item) {
                $scope.model.diagnosBeskrivning3 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning3');
                fmbService.updateFmbText(2, $scope.model.diagnosKod3);
                $scope.form2.$setDirty();
                model.updateToAttic(model.properties.form2);
            };

            /**
             * User selects a diagnose description
             */
            $scope.onDiagnoseDescription1Select = function($item) {
                $scope.model.diagnosKod = $item.value;
                $scope.model.diagnosBeskrivning1 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning1');
                fmbService.updateFmbText(0, $scope.model.diagnosKod);
                $scope.form2.$setDirty();
                model.updateToAttic(model.properties.form2);
            };
            $scope.onDiagnoseDescription2Select = function($item) {
                $scope.model.diagnosKod2 = $item.value;
                $scope.model.diagnosBeskrivning2 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning2');
                fmbService.updateFmbText(1, $scope.model.diagnosKod2);
                $scope.form2.$setDirty();
                model.updateToAttic(model.properties.form2);
            };
            $scope.onDiagnoseDescription3Select = function($item) {
                $scope.model.diagnosKod3 = $item.value;
                $scope.model.diagnosBeskrivning3 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning3');
                fmbService.updateFmbText(2, $scope.model.diagnosKod3);
                $scope.form2.$setDirty();
                model.updateToAttic(model.properties.form2);
            };


            /**
             * Limit length of field dependent on field 2 in the external model
             * @param field
             */
            $scope.limitDiagnosBeskrivningField = function(field) {
                if ($scope.model[field]) {
                    $scope.model[field] = helper.limitLength( $scope.viewState.inputLimits.diagnosBeskrivning,
                        $scope.getTotalDiagnosBeskrivningLength(), $scope.model[field]);
                }
            };


            /**
             * Calculate total length of all fields ending up in diagnosBeskrivning in the external model
             * @returns {*}
             */
            $scope.getTotalDiagnosBeskrivningLength = function() {
                var totalLength;
                totalLength = helper.getLengthOrZero($scope.model.diagnosBeskrivning) +
                helper.getLengthOrZero($scope.model.diagnosKod2) +
                helper.getLengthOrZero($scope.model.diagnosKod3) +
                helper.getLengthOrZero($scope.model.diagnosBeskrivning1) +
                helper.getLengthOrZero($scope.model.diagnosBeskrivning2) +
                helper.getLengthOrZero($scope.model.diagnosBeskrivning3);
                return totalLength;
            };

            $scope.validate = function() {
                UtkastValidationService.validate(model);
            };
        }]);
