angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form1Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            // --- form1
            $scope.$watch('cert.diabetes.insulin', function(behandlasMedInsulin) {
                if (!behandlasMedInsulin && $scope.cert.diabetes) {
                    $scope.cert.updateToAttic('diabetes.insulinBehandlingsperiod');
                    $scope.cert.diabetes.insulinBehandlingsperiod = null;

                } else {
                    $scope.cert.restoreFromAttic('diabetes.insulinBehandlingsperiod');
                }
            }, true);
            // --- form1

        }]);