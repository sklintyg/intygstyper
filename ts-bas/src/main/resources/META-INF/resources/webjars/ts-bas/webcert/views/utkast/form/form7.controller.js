angular.module('ts-bas').controller('ts-bas.Utkast.Form7Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('cert.medvetandestorning.medvetandestorning', function(harHaftMedvetandestorning) {
                if (!harHaftMedvetandestorning && $scope.cert.hjartKarl) {
                    $scope.cert.updateToAttic('medvetandestorning.beskrivning');
                    $scope.cert.medvetandestorning.beskrivning = '';
                } else {
                    $scope.cert.restoreFromAttic('medvetandestorning.beskrivning');
                }
            }, true);
            
        }]);