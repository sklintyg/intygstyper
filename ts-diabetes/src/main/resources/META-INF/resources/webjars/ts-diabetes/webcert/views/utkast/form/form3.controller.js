angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form3Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.cert = viewState.intygModel;
            $scope.viewState = viewState;

            // --- form3
            $scope.$watch('cert.syn.separatOgonlakarintyg', function(separatOgonlakarintyg) {
                if(separatOgonlakarintyg !== undefined) {
                    if (separatOgonlakarintyg && $scope.cert.syn) {
                        $scope.cert.updateToAttic('syn.synfaltsprovningUtanAnmarkning');
                        $scope.cert.updateToAttic('syn.hoger');
                        $scope.cert.updateToAttic('syn.vanster');
                        $scope.cert.updateToAttic('syn.binokulart');
                        $scope.cert.updateToAttic('syn.diplopi');

                        $scope.cert.clear('syn.synfaltsprovningUtanAnmarkning');
                        $scope.cert.clear('syn.hoger');
                        $scope.cert.clear('syn.vanster');
                        $scope.cert.clear('syn.binokulart');
                        $scope.cert.clear('syn.diplopi');
                    } else {
                        $scope.cert.restoreFromAttic('syn.synfaltsprovningUtanAnmarkning');
                        $scope.cert.restoreFromAttic('syn.hoger');
                        $scope.cert.restoreFromAttic('syn.vanster');
                        $scope.cert.restoreFromAttic('syn.binokulart');
                        $scope.cert.restoreFromAttic('syn.diplopi');
                    }
                }
            }, true);
            // --- form3

        }]);