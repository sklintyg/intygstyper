angular.module('fk7263').controller('fk7263.EditCert.Form2Ctrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService', 'fk7263.diagnosService',
        'fk7263.EditCertCtrl.Helper',
        function($log, model, $scope, viewState, diagnosService, helper) {
            'use strict';
            $scope.model = model;
            $scope.viewState = viewState;

            $scope.inputLimits = { diagnosBeskrivning :220 };

            /**
             *  Remove choices related to diagnoskoder when the choice changes to make sure
             */
            $scope.onChangeKodverk = function() {
                $scope.model.diagnosKod = '';
                $scope.model.diagnosBeskrivning1 = '';
                $scope.model.diagnosKod2 = '';
                $scope.model.diagnosBeskrivning2 = '';
                $scope.model.diagnosKod3 = '';
                $scope.model.diagnosBeskrivning3 = '';

                $scope.model.diagnosKodsystem1 = $scope.model.diagnosKodsystem1
                $scope.model.diagnosKodsystem2 = $scope.model.diagnosKodsystem1
                $scope.model.diagnosKodsystem3 = $scope.model.diagnosKodsystem1
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
                    $scope.model[field] = helper.limitLength( $scope.inputLimits.diagnosBeskrivning,
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