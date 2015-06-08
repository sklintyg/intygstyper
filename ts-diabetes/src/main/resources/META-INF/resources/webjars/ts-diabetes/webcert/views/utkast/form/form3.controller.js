angular.module('ts-diabetes').controller('ts-diabetes.Utkast.Form3Controller',
    ['$scope', '$log',
        'ts-diabetes.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            // --- form3
            $scope.$watch('viewState.intygModel.syn.separatOgonlakarintyg', function(separatOgonlakarintyg) {
                if(separatOgonlakarintyg !== undefined) {
                    if (separatOgonlakarintyg && viewState.intygModel.syn) {
                        viewState.intygModel.updateToAttic('syn.synfaltsprovningUtanAnmarkning');
                        viewState.intygModel.updateToAttic('syn.hoger');
                        viewState.intygModel.updateToAttic('syn.vanster');
                        viewState.intygModel.updateToAttic('syn.binokulart');
                        viewState.intygModel.updateToAttic('syn.diplopi');

                        viewState.intygModel.clear('syn.synfaltsprovningUtanAnmarkning');
                        viewState.intygModel.clear('syn.hoger');
                        viewState.intygModel.clear('syn.vanster');
                        viewState.intygModel.clear('syn.binokulart');
                        viewState.intygModel.clear('syn.diplopi');
                    } else {
                        viewState.intygModel.restoreFromAttic('syn.synfaltsprovningUtanAnmarkning');
                        viewState.intygModel.restoreFromAttic('syn.hoger');
                        viewState.intygModel.restoreFromAttic('syn.vanster');
                        viewState.intygModel.restoreFromAttic('syn.binokulart');
                        viewState.intygModel.restoreFromAttic('syn.diplopi');
                    }
                }
            }, true);
            // --- form3

        }]);