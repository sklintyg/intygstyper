angular.module('ts-bas').controller('ts-bas.Utkast.Form3Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('cert.funktionsnedsattning.funktionsnedsattning', function(harFunktionsnedsattning) {
                if (!harFunktionsnedsattning && $scope.cert.funktionsnedsattning) {
                    $scope.cert.updateToAttic('funktionsnedsattning.beskrivning');
                    $scope.cert.funktionsnedsattning.beskrivning = '';
                } else {
                    $scope.cert.restoreFromAttic('funktionsnedsattning.beskrivning');
                }
            }, true);

        }]);