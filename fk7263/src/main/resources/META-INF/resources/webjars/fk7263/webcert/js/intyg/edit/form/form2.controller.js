angular.module('fk7263').controller('fk7263.EditCert.Form2Ctrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService', 'fk7263.diagnosService',
        'fk7263.EditCertCtrl.Helper',
        function($log, model, $scope, viewState, diagnosService, helper) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            var diagnosKodverkStates = {ICD_10_SE:'ICD_10_SE',KSH_97_P:'KSH_97_P'}

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal) {
                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        // 2. Diagnos
                        $scope.model.diagnosKodverk1 = undefined;
                        $scope.model.diagnosKodverk2 = undefined;
                        $scope.model.diagnosKodverk3 = undefined;
                        $scope.model.diagnosKod = undefined;
                        $scope.model.diagnosKod2 = undefined;
                        $scope.model.diagnosKod3 = undefined;
                        $scope.model.diagnosBeskrivning1 = undefined;
                        $scope.model.diagnosBeskrivning2 = undefined;
                        $scope.model.diagnosBeskrivning3 = undefined;
                        $scope.model.diagnosBeskrivning = undefined;
                        $scope.model.samsjuklighet = false;
                    }
                }
            });

            $scope.$watch('viewState.common.doneLoading', function(newVal) {
                if (newVal) {
                    if (!$scope.model.diagnosKodverk) {
                        setAllDiagnosKodverk( diagnosKodverkStates.ICD_10_SE );
                    }
                }
            });

            function setAllDiagnosKodverk(val){
                $scope.model.diagnosKodverk = val;
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

                setAllDiagnosKodverk( $scope.model.diagnosKodverk );
            };

            $scope.getDiagnoseCodes = function(codeSystem, val) {
                return diagnosService.searchByCode(codeSystem, val)
                    .then(function(response) {
                        if (response && response.data && response.data.resultat === 'OK') {
                            return response.data.diagnoser.map(function(item) {
                                return {
                                    value: item.kod,
                                    beskrivning: item.beskrivning,
                                    label: item.kod + ' | ' + item.beskrivning
                                };
                            });
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
                            return response.data.diagnoser.map(function(item) {
                                return {
                                    value: item.kod,
                                    beskrivning: item.beskrivning,
                                    label: item.kod + ' | ' + item.beskrivning
                                };
                            });
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
                $scope.form2.$dirty = true;
                $scope.form2.$pristine = false;

            };
            $scope.onDiagnoseCode2Select = function($item) {
                $scope.model.diagnosBeskrivning2 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning2');
                $scope.form2.$dirty = true;
                $scope.form2.$pristine = false;
            };
            $scope.onDiagnoseCode3Select = function($item) {
                $scope.model.diagnosBeskrivning3 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning3');
                $scope.form2.$dirty = true;
                $scope.form2.$pristine = false;
            };

            /**
             * User selects a diagnose description
             */
            $scope.onDiagnoseDescription1Select = function($item) {
                $scope.model.diagnosKod = $item.value;
                $scope.model.diagnosBeskrivning1 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning1');
                $scope.form2.$dirty = true;
                $scope.form2.$pristine = false;
            };
            $scope.onDiagnoseDescription2Select = function($item) {
                $scope.model.diagnosKod2 = $item.value;
                $scope.model.diagnosBeskrivning2 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning2');
                $scope.form2.$dirty = true;
                $scope.form2.$pristine = false;
            };
            $scope.onDiagnoseDescription3Select = function($item) {
                $scope.model.diagnosKod3 = $item.value;
                $scope.model.diagnosBeskrivning3 = $item.beskrivning;
                $scope.limitDiagnosBeskrivningField('diagnosBeskrivning3');
                $scope.form2.$dirty = true;
                $scope.form2.$pristine = false;
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
                var totalLength = helper.getLengthOrZero($scope.model.diagnosBeskrivning) +
                    helper.getLengthOrZero($scope.model.diagnosKod2) +
                    helper.getLengthOrZero($scope.model.diagnosKod3) +
                    helper.getLengthOrZero($scope.model.diagnosBeskrivning1) +
                    helper.getLengthOrZero($scope.model.diagnosBeskrivning2) +
                    helper.getLengthOrZero($scope.model.diagnosBeskrivning3);
                return totalLength;
            };

        }]);