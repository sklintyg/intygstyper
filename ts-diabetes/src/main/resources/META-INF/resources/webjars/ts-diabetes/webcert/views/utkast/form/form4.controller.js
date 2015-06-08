angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form4Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            // --- form4
            $scope.$watch('viewState.intygModel.bedomning.kanInteTaStallning', function (kanInteTaStallning) {
                if (kanInteTaStallning) {
                    viewState.intygModel.updateToAttic('bedomning.korkortstyp');
                    viewState.intygModel.clear('bedomning.korkortstyp');
                } else {
                    viewState.intygModel.restoreFromAttic('bedomning.korkortstyp');
                }
            });
            // --- form4

        }]);