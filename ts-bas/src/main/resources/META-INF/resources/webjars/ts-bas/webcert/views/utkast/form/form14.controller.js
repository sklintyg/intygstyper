angular.module('ts-bas').controller('ts-bas.Utkast.Form14Controller',
    ['$scope', '$log',
        'ts-bas.UtkastController.ViewStateService',
        function($scope, $log, viewState) {
            'use strict';
            $scope.viewState = viewState;

            $scope.$watch('viewState.intygModel.sjukhusvard.sjukhusEllerLakarkontakt', function(vardatsPaSjukhus) {
                if (!vardatsPaSjukhus && viewState.intygModel.sjukhusvard) {
                    viewState.intygModel.updateToAttic('sjukhusvard.tidpunkt');
                    viewState.intygModel.updateToAttic('sjukhusvard.vardinrattning');
                    viewState.intygModel.updateToAttic('sjukhusvard.anledning');
                    viewState.intygModel.sjukhusvard.tidpunkt = '';
                    viewState.intygModel.sjukhusvard.vardinrattning = '';
                    viewState.intygModel.sjukhusvard.anledning = '';
                } else {
                    viewState.intygModel.restoreFromAttic('sjukhusvard.tidpunkt');
                    viewState.intygModel.restoreFromAttic('sjukhusvard.vardinrattning');
                    viewState.intygModel.restoreFromAttic('sjukhusvard.anledning');
                }
            }, true);
        }]);