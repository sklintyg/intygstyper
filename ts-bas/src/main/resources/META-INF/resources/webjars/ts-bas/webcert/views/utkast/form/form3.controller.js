angular.module('ts-bas').controller('ts-bas.Utkast.Form3Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.funktionsnedsattning.funktionsnedsattning', function(harFunktionsnedsattning) {
                if (!harFunktionsnedsattning && viewState.intygModel.funktionsnedsattning) {
                    viewState.intygModel.updateToAttic('funktionsnedsattning.beskrivning');
                    viewState.intygModel.funktionsnedsattning.beskrivning = '';
                } else {
                    viewState.intygModel.restoreFromAttic('funktionsnedsattning.beskrivning');
                }
            }, true);

        }]);