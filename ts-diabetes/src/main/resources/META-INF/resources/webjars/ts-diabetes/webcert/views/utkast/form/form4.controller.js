angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form4Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            // --- form4
            $scope.$watch('cert.bedomning.kanInteTaStallning', function (kanInteTaStallning) {
                if (kanInteTaStallning) {
                    $scope.cert.updateToAttic('bedomning.korkortstyp');
                    $scope.cert.clear('bedomning.korkortstyp');
                } else {
                    $scope.cert.restoreFromAttic('bedomning.korkortstyp');
                }
            });
            // --- form4

        }]);