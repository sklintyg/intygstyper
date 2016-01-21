angular.module('sjukersattning').controller('sjukersattning.EditCert.FormDiagnosCtrl',
    ['$scope', '$log', 'sjukersattning.EditCertCtrl.ViewStateService', 'sjukersattning.diagnosService', '$filter',
        'sjukersattning.EditCertCtrl.Helper',
        function($scope, $log, viewState, diagnosService, helper, $filter) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;

            $scope.viewState = viewState;

            // we skip row 1 here since its always visible and has a stanard type
            $scope.viewModel = {
                diagnosKodSystem : '',
                diagnosKodRow2 : { visible : false, diagnoseType: null },
                diagnosKodRow3 : { visible : false, diagnoseType: null }
            };

            var diagnosKodSystemStates = {ICD_10_SE:'ICD_10_SE',KSH_97_P:'KSH_97_P'};

            $scope.$watch('viewState.common.doneLoading', function(newVal, oldVal) {
                logModel();
                if(newVal === oldVal){
                    return;
                }
                if (newVal) {
                    if (model.diagnoser.length < 1 ) {
                        setAlldiagnosKodSystem( diagnosKodSystemStates.ICD_10_SE );
                    } else {
                        setAlldiagnosKodSystem( model.diagnoser[0].diagnosKodSystem );
                    }
                }

                logModel();
            });

            function setAlldiagnosKodSystem(val){
                $scope.viewModel.diagnosKodSystem = val;
                for(var i =0; i < $scope.model.diagnoser.length;i++) {
                    model.diagnoser[i].diagnosKodSystem = val;
                }

                $scope.viewModel.diagnosKodRow2.visible = false;
                $scope.viewModel.diagnosKodRow2.type = null;
                $scope.viewModel.diagnosKodRow3.visible = false;
                $scope.viewModel.diagnosKodRow3.type = null;
            }

            /**
             *  Remove choices related to diagnoskoder when the choice changes to make sure
             */
            $scope.onChangeKodverk = function() {
                resetDiagnoses();
                setAlldiagnosKodSystem( $scope.viewModel.diagnosKodSystem );
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
                $scope.model.diagnoser[0].diagnosBeskrivning = $item.beskrivning;
                $scope.model.diagnoser[0].diagnosKodSystem = $scope.viewModel.diagnosKodSystem;
                 $scope.$dirty = true;
                 $scope.$pristine = false;
                model.updateToAttic(model.properties.form4);
                //logModel();
            };
            $scope.onDiagnoseCode2Select = function($item) {
                $scope.model.diagnoser[1].diagnosBeskrivning = $item.beskrivning;
                $scope.model.diagnoser[1].diagnosKodSystem = $scope.viewModel.diagnosKodSystem;
                $scope.$dirty = true;
                $scope.$pristine = false;
                model.updateToAttic(model.properties.form4);
                //logModel();
            };
            $scope.onDiagnoseCode3Select = function($item) {
                $scope.model.diagnoser[2].diagnosBeskrivning = $item.beskrivning;
                $scope.model.diagnoser[2].diagnosKodSystem = $scope.viewModel.diagnosKodSystem;
                $scope.$dirty = true;
                $scope.$pristine = false;
                model.updateToAttic(model.properties.form4);
                //logModel();
            };


            /**
             * User selects a diagnose description
             */
            $scope.onDiagnoseDescription1Select = function($item) {
                $scope.model.diagnoser[0].diagnosKod = $item.value;
                $scope.model.diagnoser[0].diagnosBeskrivning = $item.beskrivning;
                $scope.model.diagnoser[0].diagnosKodSystem = $scope.viewModel.diagnosKodSystem;
                $scope.$dirty = true;
                $scope.$pristine = false;
                $scope.model.updateToAttic(model.properties.form4);
                //logModel();
            };
            $scope.onDiagnoseDescription2Select = function($item) {
                $scope.model.diagnoser[1].diagnosKod  = $item.value;
                $scope.model.diagnoser[1].diagnosBeskrivning = $item.beskrivning;
                $scope.model.diagnoser[1].diagnosKodSystem = $scope.viewModel.diagnosKodSystem;
                $scope.$dirty = true;
                $scope.$pristine = false;
                $scope.model.updateToAttic(model.properties.form4);
                //logModel();
            };
            $scope.onDiagnoseDescription3Select = function($item) {
                $scope.model.diagnoser[2].diagnosKod  = $item.value;
                $scope.model.diagnoser[2].diagnosBeskrivning = $item.beskrivning;
                $scope.model.diagnoser[2].diagnosKodSystem = $scope.viewModel.diagnosKodSystem;
                $scope.$dirty = true;
                $scope.$pristine = false;
                $scope.model.updateToAttic(model.properties.form4);
                //logModel();
            };

            function logModel(){
                console.log('model diagnos' + JSON.stringify( $scope.model.diagnoser ));
            }



            /**
             * Limit length of field dependent on field 2 in the external model
             * @param field
             */
           /* $scope.limitDiagnosBeskrivningField = function(num) {
                var matchDiagnoser = $filter('filter')(model.diagnoser, {funktionsomrade:funktionsomrade})[0];
                if ($scope.model.diagnoser[num].diagnosBeskrivning) {
                    $scope.model.diagnoser[num].diagnosBeskrivning = helper.limitLength( $scope.viewState.inputLimits.diagnosBeskrivning,
                        $scope.getTotalDiagnosBeskrivningLength(), $scope.model.diagnoser[num].diagnosBeskrivning);
                }
            }; */


            /**
             * Calculate total length of all fields ending up in diagnosBeskrivning in the external model
             * @returns {*}
             */
            $scope.getTotalDiagnosBeskrivningLength = function() {
                var totalLength;
                totalLength = helper.getLengthOrZero($scope.model.diagnoser[1].diagnosKod) +
                helper.getLengthOrZero($scope.model.diagnoser[2].diagnosKod) +
                helper.getLengthOrZero($scope.model.diagnoser[0].diagnosBeskrivning) +
                helper.getLengthOrZero($scope.model.diagnoser[1].diagnosBeskrivning);
               // helper.getLengthOrZero($scope.model.diagnoser[2].diagnosBeskrivning);
                return totalLength;
            };

            function resetDiagnoses(){
                $scope.model.diagnoser.forEach(function(diagnos) {
                    diagnos.diagnosKodSystem = undefined;
                    diagnos.diagnosKod = undefined;
                    diagnos.diagnosBeskrivning = undefined;
                });
            }

        }]);