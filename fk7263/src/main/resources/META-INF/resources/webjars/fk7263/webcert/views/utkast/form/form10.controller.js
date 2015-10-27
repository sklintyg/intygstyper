angular.module('fk7263').controller('fk7263.EditCert.Form10Ctrl',
    ['$scope', '$log', 'fk7263.EditCertCtrl.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            var model = viewState.intygModel;
            $scope.model = model;

            $scope.viewState = viewState;

            var prognosStates = {NO: 'NO', YES: 'YES', PARTLY: 'PARTLY', UNKNOWN: 'UNKNOWN'};

            $scope.radioGroups = {
                prognos: prognosStates.YES
            };

            // lifecyle listeners --------------------------------------------------------------------------------------

            // once we've doneLoading we can set the radio buttons to the model state.
            $scope.$on('fk7263.loaded', function() {
                radioGroupNotSelectedCheck();
                setPrognosGroupFromModel();
            });

            $scope.$watch('viewState.sysselsattningValue', function(newVal, oldVal) {
                if (JSON.stringify(newVal) === JSON.stringify(oldVal)) {
                    return;
                }

                // only do this once the page is loaded and changes come from the gui!
                if(viewState.common.doneLoading) {
                    // If only arbetsloshet in frpm 8a i set, then form 10 is hidden
                    // and we need to store its state in the attic
                    if (JSON.stringify(newVal) === JSON.stringify([false, true, false])) {
                        model.updateToAttic(model.properties.form10);
                        model.clear(model.properties.form10);
                        model.prognosBedomning = undefined;
                    } else if(model.isInAttic(model.properties.form10)){
                        model.restoreFromAttic(model.properties.form10);
                    }
                    setPrognosGroupFromModel();
                }
            });

            // view/scope methods --------------------------------------------------------------------------------------

            function radioGroupNotSelectedCheck(){
                if (model.prognosBedomning === undefined) {
                    model.prognosBedomning = 'arbetsformagaPrognosJa';
                }
            }

            function setPrognosGroupFromModel() {
                switch (model.prognosBedomning) {
                case 'arbetsformagaPrognosJaDelvis':
                    $scope.radioGroups.prognos = prognosStates.PARTLY;
                    break;
                case 'arbetsformagaPrognosNej':
                    $scope.radioGroups.prognos = prognosStates.NO;
                    break;
                case 'arbetsformagaPrognosGarInteAttBedoma':
                    $scope.radioGroups.prognos = prognosStates.UNKNOWN;
                    break;
                case 'arbetsformagaPrognosJa':
                    /* falls through */
                default :
                    $scope.radioGroups.prognos = prognosStates.YES;
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
                model.updateToAttic(model.properties.form10);
            };

            $scope.showInteAttBedoma = function() {
                return $scope.radioGroups.prognos === prognosStates.UNKNOWN;
            };

        }]);
