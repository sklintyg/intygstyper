angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form1Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            // --- form1
            $scope.$watch('viewState.intygModel.diabetes.insulin', function(behandlasMedInsulin) {
                if (!behandlasMedInsulin && viewState.intygModel.diabetes) {
                    viewState.intygModel.updateToAttic('diabetes.insulinBehandlingsperiod');
                    viewState.intygModel.diabetes.insulinBehandlingsperiod = null;

                } else {
                    viewState.intygModel.restoreFromAttic('diabetes.insulinBehandlingsperiod');
                }
            }, true);
            // --- form1

        }]);