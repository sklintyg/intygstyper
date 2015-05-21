angular.module('ts-bas').controller('ts-bas.Utkast.Form15Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            $scope.$watch('cert.medicinering.stadigvarandeMedicinering', function(harStadigvarandeMedicinering) {
                if (!harStadigvarandeMedicinering && $scope.cert.medicinering) {
                    $scope.cert.updateToAttic('medicinering.beskrivning');
                    $scope.cert.medicinering.beskrivning = '';
                } else {
                    $scope.cert.restoreFromAttic('medicinering.beskrivning');
                }
            }, true);
        }]);