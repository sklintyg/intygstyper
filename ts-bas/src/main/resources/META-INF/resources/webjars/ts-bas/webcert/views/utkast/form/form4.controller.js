angular.module('ts-bas').controller('ts-bas.Utkast.Form4Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.hjartKarl.riskfaktorerStroke', function(foreliggerRiskfaktorerStroke) {
                if (!foreliggerRiskfaktorerStroke && viewState.intygModel.hjartKarl) {
                    viewState.intygModel.updateToAttic('hjartKarl.beskrivningRiskfaktorer');
                    viewState.intygModel.hjartKarl.beskrivningRiskfaktorer = '';
                } else {
                    viewState.intygModel.restoreFromAttic('hjartKarl.beskrivningRiskfaktorer');
                }
            }, true);
            
        }]);