angular.module('fk7263').controller('fk7263.EditCert.Form10Ctrl',
    ['$log', 'fk7263.Domain.IntygModel', '$scope', 'fk7263.EditCertCtrl.ViewStateService',
        function($log, model, $scope, viewState) {
            'use strict';
            $scope.model = model;

            // this is set by ng-form
            $scope.attic = {
                arbetsformagaPrognosGarInteAttBedomaBeskrivning: '',
                prognosBedomning: ''
            };

            $scope.viewState = viewState;

            var prognosStates = {NO: 'NO', YES: 'YES', PARTLY: 'PARTLY', UNKNOWN: 'UNKNOWN'};

            $scope.radioGroups = {
                prognos: prognosStates.YES
            };

            // lifecyle listeners --------------------------------------------------------------------------------------

            // once we've doneLoading we can set the radion buttons to the model state.
            $scope.$on('fk7263.loaded', function() {
                setPrognosGroupFromModel();
            });

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal) {
                // only do this once the page is loaded and changes come from the gui!
                if (viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        clearModel();
                    } else {
                        if($scope.attic.prognosBedomning){
                            restoreFromAttic();
                        } else if (!$scope.radioGroups.prognos || $scope.radioGroups.prognos.length === 0) {
                            model.prognosBedomning = 'arbetsformagaPrognosJa';
                        }
                    }
                    setPrognosGroupFromModel();
                }
            });

            // view/scope methods --------------------------------------------------------------------------------------

            function setPrognosGroupFromModel() {
                switch (model.prognosBedomning) {
                case 'arbetsformagaPrognosJa':
                    $scope.radioGroups.prognos = prognosStates.YES;
                    break;
                case 'arbetsformagaPrognosJaDelvis':
                    $scope.radioGroups.prognos = prognosStates.PARTLY;
                    break;
                case 'arbetsformagaPrognosNej':
                    $scope.radioGroups.prognos = prognosStates.NO;
                    break;
                case 'arbetsformagaPrognosGarInteAttBedoma':
                    $scope.radioGroups.prognos = prognosStates.UNKNOWN;
                    break;
                default :
                    $scope.radioGroups.prognos = undefined;
                }
            }

            $scope.onPrognosChange = function() {
                switch ($scope.radioGroups.prognos) {
                case prognosStates.YES:
                    model.prognosBedomning = 'arbetsformagaPrognosJa';
                    break;
                case prognosStates.NO:
                    model.prognosBedomning = 'arbetsformagaPrognosNej';
                    break;
                case prognosStates.PARTLY:
                    model.prognosBedomning = 'arbetsformagaPrognosJaDelvis';
                    break;
                case prognosStates.UNKNOWN:
                    model.prognosBedomning = 'arbetsformagaPrognosGarInteAttBedoma';
                    break;
                default :
                    model.prognosBedomning = undefined;
                }
                updateAttic();
            };

            $scope.showInteAttBedoma = function() {
                return $scope.radioGroups.prognos === prognosStates.UNKNOWN;
            };

            // utils ---------------------------------------------------------------------------------------------------
            function clearModel(){
                model.prognosBedomning = undefined;
                model.arbetsformagaPrognosGarInteAttBedomaBeskrivning = undefined;
            }

            function updateAttic(){
                $scope.attic.prognosBedomning = model.prognosBedomning;
                $scope.attic.arbetsformagaPrognosGarInteAttBedomaBeskrivning = model.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
            }

            function restoreFromAttic(){
                model.prognosBedomning = $scope.attic.prognosBedomning;
                model.arbetsformagaPrognosGarInteAttBedomaBeskrivning = $scope.attic.arbetsformagaPrognosGarInteAttBedomaBeskrivning;
            }

        }]);