angular.module('ts-bas').controller('ts-bas.Utkast.Form15Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.medicinering.stadigvarandeMedicinering', function(harStadigvarandeMedicinering) {
                if (!harStadigvarandeMedicinering && viewState.intygModel.medicinering) {
                    viewState.intygModel.updateToAttic('medicinering.beskrivning');
                    viewState.intygModel.medicinering.beskrivning = '';
                } else {
                    viewState.intygModel.restoreFromAttic('medicinering.beskrivning');
                }
            }, true);
        }]);