angular.module('ts-bas').controller('ts-bas.Utkast.Form4Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('cert.hjartKarl.riskfaktorerStroke', function(foreliggerRiskfaktorerStroke) {
                if (!foreliggerRiskfaktorerStroke && $scope.cert.hjartKarl) {
                    $scope.cert.updateToAttic('hjartKarl.beskrivningRiskfaktorer');
                    $scope.cert.hjartKarl.beskrivningRiskfaktorer = '';
                } else {
                    $scope.cert.restoreFromAttic('hjartKarl.beskrivningRiskfaktorer');
                }
            }, true);
            
        }]);