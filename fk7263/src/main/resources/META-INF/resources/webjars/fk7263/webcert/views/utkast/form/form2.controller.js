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
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService', 'fk7263.diagnosService', 'common.fmbService', 'common.fmb.ViewStateService',
        'fk7263.EditCertCtrl.Helper',
        function($scope, $log, viewState, diagnosService, fmbService, fmbViewState, helper) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;

            $scope.viewState = viewState;

            $scope.fmb = fmbViewState.state;

            $scope.viewModel = {
                diagnosKodverk : ''
            };

            var diagnosKodverkStates = {ICD_10_SE:'ICD_10_SE',KSH_97_P:'KSH_97_P'};

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
                    $scope.updateFmbText();
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

                $scope.updateFmbText();

                setAllDiagnosKodverk( $scope.viewModel.diagnosKodverk );
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

            //What we do if the call to the FMB service is successful
            var fmbSuccess = function fmbSuccess(formData) {
                fmbViewState.setState(formData, $scope.model.diagnosKod);
            };

            var fmbReject = function fmbReject(data) {
                $log.debug('Error searching fmb help text');
                $log.debug(data);
                return [];
            };

            $scope.updateFmbText = function() {
                if ($scope.model.diagnosKod === undefined || $scope.model.diagnosKod.length === 0) {
                    fmbViewState.reset();
                } else if(fmbViewState.state.diagnosKod !== $scope.model.diagnosKod) {
                    fmbService.getFMBHelpTextsByCode($scope.model.diagnosKod).then(fmbSuccess, fmbReject);
                }
            };

            /**
             * User selects a diagnose code
             */
            $scope.onDiagnoseCode1Select = function($item) {
                $scope.model.diagnosBeskrivning1 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning1');

                $scope.updateFmbText();

                $scope.form2.$setDirty();
                model.updateToAttic(model.properties.form2);
            };
            $scope.onDiagnoseCode2Select = function($item) {
                $scope.model.diagnosBeskrivning2 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning2');
                $scope.form2.$setDirty();
                model.updateToAttic(model.properties.form2);
            };
            $scope.onDiagnoseCode3Select = function($item) {
                $scope.model.diagnosBeskrivning3 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning3');
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

                $scope.form2.$setDirty();
                $scope.updateFmbText();

                model.updateToAttic(model.properties.form2);
            };
            $scope.onDiagnoseDescription2Select = function($item) {
                $scope.model.diagnosKod2 = $item.value;
                $scope.model.diagnosBeskrivning2 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning2');
                $scope.form2.$setDirty();
                model.updateToAttic(model.properties.form2);
            };
            $scope.onDiagnoseDescription3Select = function($item) {
                $scope.model.diagnosKod3 = $item.value;
                $scope.model.diagnosBeskrivning3 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning3');
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

        }]);
