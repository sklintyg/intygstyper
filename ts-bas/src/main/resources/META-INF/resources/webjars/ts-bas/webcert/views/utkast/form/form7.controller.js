angular.module('ts-bas').controller('ts-bas.Utkast.Form7Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.medvetandestorning.medvetandestorning', function(harHaftMedvetandestorning) {
                if (!harHaftMedvetandestorning && viewState.intygModel.hjartKarl) {
                    viewState.intygModel.updateToAttic('medvetandestorning.beskrivning');
                    viewState.intygModel.medvetandestorning.beskrivning = '';
                } else {
                    viewState.intygModel.restoreFromAttic('medvetandestorning.beskrivning');
                }
            }, true);
        }]);