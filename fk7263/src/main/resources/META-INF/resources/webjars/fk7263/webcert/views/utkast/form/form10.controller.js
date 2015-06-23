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

            // once we've doneLoading we can set the radion buttons to the model state.
            $scope.$on('fk7263.loaded', function() {
                radioGroupNotSelectedCheck();
                setPrognosGroupFromModel();
            });

            $scope.$watch('viewState.avstangningSmittskyddValue', function(newVal, oldVal) {
                if(newVal === oldVal){
                    return;
                }
                // only do this once the page is loaded and changes come from the gui!
                if (viewState.common.doneLoading) {
                    // Remove defaults not applicable when smittskydd is active
                    if (newVal === true) {
                        model.updateToAttic(model.properties.form10);
                        model.clear(model.properties.form10);
                    } else {
                        if(model.isInAttic(model.properties.form10)){
                            model.restoreFromAttic(model.properties.form10);
                        }
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
                model.updateToAttic(model.properties.form10);
            };

            $scope.showInteAttBedoma = function() {
                return $scope.radioGroups.prognos === prognosStates.UNKNOWN;
            };

        }]);